package com.five.Maeum_Eum.controller.center;

import com.five.Maeum_Eum.dto.AddressDTO;
import com.five.Maeum_Eum.exception.ErrorResponse;
import com.five.Maeum_Eum.service.center.KakaoAddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/center")
public class CenterController {

    private final KakaoAddressService kakaoAddressService;

    @PostMapping
    public ResponseEntity<Object> get(@RequestBody AddressDTO addressDTO) {
        System.out.println(addressDTO.getAddress());
        return ResponseEntity.ok().body(kakaoAddressService.getCoordinates(addressDTO.getAddress()));
    }
}
