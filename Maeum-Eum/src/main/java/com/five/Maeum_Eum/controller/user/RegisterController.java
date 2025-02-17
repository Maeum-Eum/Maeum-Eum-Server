package com.five.Maeum_Eum.controller.user;

import com.five.Maeum_Eum.dto.user.login.request.UserLoginDTO;
import com.five.Maeum_Eum.dto.user.login.response.UserInfoDTO;
import com.five.Maeum_Eum.dto.user.manager.response.ManagerBasicDto;
import com.five.Maeum_Eum.dto.user.register.request.CaregiverRegiDTO;
import com.five.Maeum_Eum.dto.user.register.request.ManagerRegiDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.exception.ErrorResponse;
import com.five.Maeum_Eum.service.user.RegisterService;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;
    private final CaregiverService caregiverService;

    /* 사회복지사 (관리자) 회원가입*/
    @PostMapping("/manager/register")
    public ResponseEntity<ManagerBasicDto> manager(@RequestBody ManagerRegiDTO regiDTO) {
        ManagerBasicDto managerBasicDto = registerService.registerManager(regiDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(managerBasicDto);
    }
    @PostMapping("/caregiver/register")
    public ResponseEntity<Object> caregiver(@RequestBody CaregiverRegiDTO regiDTO) {
        if (registerService.registerCaregiver(regiDTO)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity
                .status(400)
                .body(ErrorResponse.builder()
                        .code("InvalidInput")
                        .status(HttpStatus.BAD_REQUEST)
                        .message("잘못된 입력이 있습니다. 입력을 확인해주세요")
                        .build());
    }

    // 프로필 최초 업로드 ( 사용자가 생성되기 전이므로 인증 절차가 없습니다 )
    @PostMapping("/caregiver/register/{caregiver}")
    public ResponseEntity<Object> uploadProfile(@PathVariable String caregiver,
                                                @RequestParam("profileImage") MultipartFile file) throws IllegalAccessException {

        Caregiver caregiver1 = caregiverService.findCaregiverByLoginId(caregiver);
        if (caregiver1 == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        caregiverService.uploadProfileImage(file, caregiver1);
        return ResponseEntity.ok().build();
    }

    // id 중복 검사
    @PostMapping("/validateID")
    public ResponseEntity<Object> validateDuple(@RequestBody UserLoginDTO dto) {

        if (registerService.validateDuplicateID(dto.getId())) {
            return ResponseEntity
                    .status(400)
                    .body(ErrorResponse.builder()
                            .code("InvalidInput")
                            .status(HttpStatus.BAD_REQUEST)
                            .message("중복된 아이디입니다")
                            .build());
        }

        else {
            return ResponseEntity.status(200).body(dto);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserInfo() {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(UserInfoDTO.builder()
                .role(role)
                .build());

    }
}
