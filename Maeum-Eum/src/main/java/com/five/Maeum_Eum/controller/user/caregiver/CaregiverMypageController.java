package com.five.Maeum_Eum.controller.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.mypage.CaregiverMypageDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.exception.ErrorResponse;
import com.five.Maeum_Eum.jwt.CaregiverUserDetails;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/caregiver/mypage")
public class CaregiverMypageController {

    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;

    @GetMapping()
    public ResponseEntity<Object> mypage(String token) {

        String role = jwtUtil.getRole(token);


        if(findCaregiver == null) {
            return ResponseEntity
                    .status(400)
                    .body(ErrorResponse.builder()
                            .code("Caregiver Not Found")
                            .status(HttpStatus.BAD_REQUEST)
                            .message("유저 정보를 가져오지 못했습니다.")
                            .build());
        }

        Resume resume = findCaregiver.getResume();

        CaregiverMypageDTO dto = CaregiverMypageDTO.builder()
                .name(findCaregiver.getName())
                .address(findCaregiver.getAddress())
                .savedEldersCount(findCaregiver.getSavedElders().size())
                .managerContactCount(findCaregiver.getManagerContact().size())
                .isJobOpen(findCaregiver.isJobOpen())
                .ProfileImage(resume.getProfileImage())
                .isResumeRegistered(findCaregiver.isResumeRegistered())
                .build();

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/job-open")
    public ResponseEntity<Object> jobopen(@AuthenticationPrincipal CaregiverUserDetails userDetails) {

        String caregiverId = userDetails.getUsername();

        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(caregiverId);
        if(findCaregiver == null) {
            return ResponseEntity
                    .status(400)
                    .body(ErrorResponse.builder()
                            .code("Caregiver Not Found")
                            .status(HttpStatus.BAD_REQUEST)
                            .message("유저 정보를 가져오지 못했습니다.")
                            .build());
        }

        findCaregiver.toggleJobOpenState();
        return ResponseEntity.ok().build();
    }
}