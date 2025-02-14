package com.five.Maeum_Eum.controller;

import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.exception.ErrorResponse;
import com.five.Maeum_Eum.repository.center.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CenterController {

    private final CenterRepository centerRepository;

    @GetMapping("/center")
    public ResponseEntity<Object> searchCenter(@RequestParam String name) {
        Center center = centerRepository.findByCenterName(name).orElse(null);

        if(center == null) {
            return ResponseEntity
                    .status(400)
                    .body(ErrorResponse.builder()
                            .code("Center Not Found")
                            .status(HttpStatus.BAD_REQUEST)
                            .message("센터 정보를 가져오지 못했습니다.")
                            .build());
        }
        return ResponseEntity.ok(null);
    }
}