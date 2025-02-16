package com.five.Maeum_Eum.controller.user.manager;

import com.five.Maeum_Eum.dto.user.manager.response.ManagerBasicDto;
import com.five.Maeum_Eum.service.user.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    /* 관리자 마이페이지 */
    @GetMapping("/mypage")
    public ResponseEntity<ManagerBasicDto> getManagerMypage(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7).trim();
         ManagerBasicDto managerBasicDto = managerService.getManagerBasicInfo(token);
         return ResponseEntity.status(HttpStatus.OK)
                 .body(managerBasicDto);
    }



}
