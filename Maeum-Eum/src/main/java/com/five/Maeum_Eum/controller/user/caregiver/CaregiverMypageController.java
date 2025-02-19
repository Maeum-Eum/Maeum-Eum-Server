package com.five.Maeum_Eum.controller.user.caregiver;

import com.five.Maeum_Eum.common.PageResponse;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.SimpleContactDTO;
import com.five.Maeum_Eum.dto.user.caregiver.mypage.CaregiverMypageDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverMypageService;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/caregiver/mypage")
public class CaregiverMypageController {

    private final CaregiverMypageService caregiverMypageService;
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

        CaregiverMypageDTO dto = caregiverMypageService.getDto(findCaregiver);
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

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<Object> profile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7).trim();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
        }

        Resume resume = findCaregiver.getResume();
        if(resume == null) {
            throw new CustomException(ErrorCode.RESUME_NOT_REGISTERED, "프로필 사진이 등록되지 않은 사용자입니다.");
        }

        Map<String, String> response = new HashMap<>();
        response.put("profileImage",resume.getProfileImage());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<Object> uploadProfileImage(@RequestHeader("Authorization") String authHeader,
                                                     @RequestParam("profileImage") MultipartFile file) throws IllegalAccessException {

        String token = authHeader.substring(7).trim();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
        }

        String result = caregiverService.uploadProfileImage(file, findCaregiver);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<String> deleteProfileImage(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7).trim();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
        }

        String result = caregiverService.deleteProfileImage(findCaregiver);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/contacts")
    public ResponseEntity<Object> getContacts(@PageableDefault(size = 5) Pageable pageable,
                                          @RequestParam String tab) {
        ApprovalStatus approvalStatus;
        if (tab.equals("pending")) {
            approvalStatus = ApprovalStatus.PENDING;
        }

        else if (tab.equals("approved")) {
            approvalStatus = ApprovalStatus.APPROVED;
        }
        else throw new CustomException(ErrorCode.INVALID_INPUT);
        PageResponse<SimpleContactDTO> body = caregiverService.getContacts(pageable, approvalStatus);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/applies")
    public ResponseEntity<Object> getApplies(@PageableDefault(size = 5) Pageable pageable,
                                              @RequestParam String tab) {
        ApprovalStatus approvalStatus;
        if (tab.equals("pending")) {
            approvalStatus = ApprovalStatus.PENDING;
        }

        else if (tab.equals("approved")) {
            approvalStatus = ApprovalStatus.APPROVED;
        }
        else throw new CustomException(ErrorCode.INVALID_INPUT);
        PageResponse<SimpleContactDTO> body = caregiverService.getApplies(pageable, approvalStatus);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<Object> getBookmarks(@PageableDefault(size = 5) Pageable pageable) {
        PageResponse<SimpleContactDTO> body = caregiverService.getBookmark(pageable);
        return ResponseEntity.ok().body(body);
    }
}