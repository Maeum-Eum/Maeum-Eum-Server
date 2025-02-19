package com.five.Maeum_Eum.controller.user.elder;

import com.five.Maeum_Eum.dto.user.caregiver.main.response.RecommendedCaregiverDto;
import com.five.Maeum_Eum.dto.user.elder.request.ElderCreateDTO;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.service.user.elder.ElderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/elder")
public class ElderController {

    private final ElderService elderService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody ElderCreateDTO dto) {
        if(elderService.createElder(dto)) {
            return ResponseEntity.ok(dto);
        }

        throw new CustomException(ErrorCode.INVALID_INPUT);
    }

    @GetMapping("/edit")
    public ResponseEntity<Object> get(@RequestParam Long elderId){
        ElderCreateDTO body = elderService.getElder(elderId);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/edit")
    public ResponseEntity<Object> edit(@RequestParam Long elderId,
                                       @RequestBody ElderCreateDTO dto){
        elderService.editElder(dto, elderId);
        return ResponseEntity.ok().build();
    }
}
