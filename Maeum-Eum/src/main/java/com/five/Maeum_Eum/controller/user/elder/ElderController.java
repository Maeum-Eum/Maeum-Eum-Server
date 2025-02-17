package com.five.Maeum_Eum.controller.user.elder;

import com.five.Maeum_Eum.common.PageResponse;
import com.five.Maeum_Eum.dto.user.elder.request.ElderCreateDTO;
import com.five.Maeum_Eum.dto.user.manager.response.ToCaregiverDTO;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.service.user.elder.ElderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
