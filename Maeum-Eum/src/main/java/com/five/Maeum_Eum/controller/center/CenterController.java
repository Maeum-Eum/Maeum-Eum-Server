package com.five.Maeum_Eum.controller.center;

import com.five.Maeum_Eum.dto.AddressDTO;
import com.five.Maeum_Eum.dto.center.request.ModifyCenterReq;
import com.five.Maeum_Eum.dto.center.response.CenterDTO;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.exception.ErrorResponse;
import com.five.Maeum_Eum.repository.center.CenterRepository;
import com.five.Maeum_Eum.service.center.CenterService;
import com.five.Maeum_Eum.service.center.KakaoAddressService;
import com.five.Maeum_Eum.service.user.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/center")
public class CenterController {

    private final KakaoAddressService kakaoAddressService;
    private final CenterRepository centerRepository;
    private final CenterService centerService;
    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<Object> get(@RequestBody AddressDTO addressDTO) {
        System.out.println(addressDTO.getAddress());
        return ResponseEntity.ok().body(kakaoAddressService.getCoordinates(addressDTO.getAddress()));
    }

    @GetMapping
    public ResponseEntity<Object> searchCenter(@RequestParam("keyword") String keyword) {

        List<Center> centerList= centerRepository.searchCentersByKeyword(keyword);

        if(centerList.isEmpty()) {
            return ResponseEntity
                    .status(400)
                    .body(ErrorResponse.builder()
                            .code("Center Not Found")
                            .status(HttpStatus.BAD_REQUEST)
                            .message("센터 정보를 가져오지 못했습니다.")
                            .build());
        }

        // Center 리스트를 CenterDTO 리스트로 변환
        List<CenterDTO> dtoList = centerList.stream()
                .map(center -> CenterDTO.builder()
                        .centerId(center.getCenterId())
                        .centerName(center.getCenterName())
                        .address(center.getAddress())
                        .designatedTime(center.getDesignatedTime())
                        .installationTime(center.getInstallationTime())
                        .detailAddress(center.getDetailAddress())
                        .zipCode(center.getZipCode())
                        .centerCode(center.getCenterCode())
                        .finalGrade(center.getFinalGrade())
                        .oneLineIntro(center.getOneLineIntro())
                        .build())
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    /*관리자가 소속된 센터 조회*/
    @GetMapping("/{centerId}")
    public ResponseEntity<CenterDTO> getCenterInfo(@PathVariable("centerId") Long centerId){
        CenterDTO centerDTO = centerService.getCenterInfo(centerId);
        return ResponseEntity.ok(centerDTO);
    }

    /* 센터 정보 중 한 줄 소개 수정 */
    @PatchMapping("/{centerId}")
    public  ResponseEntity<CenterDTO> modifyCenterInfo(@RequestHeader("Authorization") String authHeader , @PathVariable("centerId") Long centerId ,
                                                       @RequestBody ModifyCenterReq modifyCenterReq){
        String token = authHeader.substring(7).trim();
        CenterDTO centerDTO = managerService.modifyCenterInfo(token , centerId , modifyCenterReq);
        return ResponseEntity.ok(centerDTO);
    }
}
