package com.five.Maeum_Eum.controller.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.mypage.CaregiverMypageDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/caregiver/mypage")
public class CaregiverMypageController {

    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<Object> mypage(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7).trim();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
        }

        CaregiverMypageDTO dto = CaregiverMypageDTO.builder()
                .name(findCaregiver.getName())
                .address(findCaregiver.getAddress())
                .savedEldersCount(findCaregiver.getSavedElders().size())
                .managerContactCount(findCaregiver.getManagerContact().size())
                .isJobOpen(findCaregiver.isJobOpen())
                .ProfileImage(findCaregiver.isResumeRegistered() ? findCaregiver.getResume().getProfileImage() : "")
                .isResumeRegistered(findCaregiver.isResumeRegistered())
                .build();

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/job-open")
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<Object> jobopen(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7).trim();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
        }

        Map<String, Boolean> response = new HashMap<>();
        caregiverService.toggleJobOpenState(findCaregiver);
        response.put("isJobOpen",findCaregiver.isJobOpen());
        return ResponseEntity.ok(response);
    }
}