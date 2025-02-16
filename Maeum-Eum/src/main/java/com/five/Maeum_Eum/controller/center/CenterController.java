package com.five.Maeum_Eum.controller.center;

import com.five.Maeum_Eum.dto.AddressDTO;
import com.five.Maeum_Eum.dto.center.response.CenterDTO;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.exception.ErrorResponse;
import com.five.Maeum_Eum.repository.center.CenterRepository;
import com.five.Maeum_Eum.service.center.CenterService;
import com.five.Maeum_Eum.service.center.KakaoAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/center")
public class CenterController {

    private final KakaoAddressService kakaoAddressService;
    private final CenterRepository centerRepository;
    private final CenterService centerService;

    @PostMapping
    public ResponseEntity<Object> get(@RequestBody AddressDTO addressDTO) {
        System.out.println(addressDTO.getAddress());
        return ResponseEntity.ok().body(kakaoAddressService.getCoordinates(addressDTO.getAddress()));
    }

    @GetMapping
    public ResponseEntity<Object> searchCenter(@RequestParam("name") String name) {

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

        CenterDTO dto = CenterDTO.builder()
                .centerId(center.getCenterId())
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .designatedTime(center.getDesignatedTime())
                .installationTime(center.getInstallationTime())
                .detailAddress(center.getDetailAddress())
                .zipCode(center.getZipCode())
                .centerCode(center.getCenterCode())
                .build();

        return ResponseEntity.ok(dto);
    }


    /*관리자가 소속된 센터 조회*/
    @GetMapping("/{centerId}")
    public ResponseEntity<CenterDTO> getCenterInfo(@PathVariable("centerId") Long centerId){
        CenterDTO centerDTO = centerService.getCenterInfo(centerId);
        return ResponseEntity.ok(centerDTO);
    }
}
