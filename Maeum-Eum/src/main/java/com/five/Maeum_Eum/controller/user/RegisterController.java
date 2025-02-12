package com.five.Maeum_Eum.controller.user;

import com.five.Maeum_Eum.dto.user.register.request.CaregiverRegiDTO;
import com.five.Maeum_Eum.dto.user.register.request.ManagerRegiDTO;
import com.five.Maeum_Eum.service.user.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/manager/register")
    public ResponseEntity<Object> manager(@RequestBody ManagerRegiDTO regiDTO) {
        if (registerService.registerManager(regiDTO)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/caregiver/register")
    public ResponseEntity<Object> caregiver(@RequestBody CaregiverRegiDTO regiDTO) {
        if (registerService.registerCaregiver(regiDTO)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
