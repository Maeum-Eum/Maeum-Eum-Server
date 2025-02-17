package com.five.Maeum_Eum.controller.user.manager;

import com.five.Maeum_Eum.dto.center.request.ChangeCenterReq;
import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ResumeResponseDTO;
import com.five.Maeum_Eum.dto.user.manager.response.ManagerBasicDto;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import com.five.Maeum_Eum.service.user.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final CaregiverService caregiverService;

    private String extractToken(String authHeader){
        return authHeader.substring(7).trim();
    }

    /* 관리자 마이페이지 */
    @GetMapping("/mypage")
    public ResponseEntity<ManagerBasicDto> getManagerMypage(@RequestHeader("Authorization") String authHeader){
        String token = extractToken(authHeader);
         ManagerBasicDto managerBasicDto = managerService.getManagerBasicInfo(token);
         return ResponseEntity.status(HttpStatus.OK)
                 .body(managerBasicDto);
    }

    /* 관리자가 소속된 센터 변경 */
    @PatchMapping("/center")
    public ResponseEntity<ManagerBasicDto> changerCenter(@RequestHeader("Authorization") String authHeader , @RequestBody ChangeCenterReq centerReq){
        String token = extractToken(authHeader);
        ManagerBasicDto managerBasicDto = managerService.changeCenter(token , centerReq);
        return ResponseEntity.ok(managerBasicDto);
    }


    /* 관리자가 요양보호사의 이력서 조회 */
    @GetMapping("/{caregiverId}")
    public ResponseEntity<ResumeResponseDTO> getCaregiverResume(@RequestHeader("Authorization") String authHeader , @PathVariable("caregiverId")Long caregiverId){
        String token = extractToken(authHeader);
        ResumeResponseDTO responseDTO = caregiverService.getCaregiverResume(token , caregiverId);
        return  ResponseEntity.ok(responseDTO);
    }





}
