package com.five.Maeum_Eum.service.center;

import com.five.Maeum_Eum.dto.center.response.CenterDTO;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.repository.center.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CenterService {

    private final CenterRepository centerRepository;

    /* 관리자의 센터 정보 찾기 */
    public CenterDTO getCenterInfo(Long centerId) {
        Center center = centerRepository.findByCenterId(centerId).orElseThrow(() -> new CustomException(ErrorCode.CENTER_NOT_FOUND));
        CenterDTO centerDTO = CenterDTO.builder()
                .centerId(center.getCenterId())
                .centerName(center.getCenterName())
                .centerCode(center.getCenterCode())
                .zipCode(center.getZipCode())
                .designatedTime(center.getDesignatedTime())
                .installationTime(center.getInstallationTime())
                .detailAddress(center.getDetailAddress())
                .address(center.getAddress())
                .build();

        return centerDTO;

    }
}
