package com.five.Maeum_Eum.service.user;

import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ExperienceDTO;
import com.five.Maeum_Eum.dto.user.manager.response.ManagerBasicDto;
import com.five.Maeum_Eum.dto.user.register.request.CaregiverRegiDTO;
import com.five.Maeum_Eum.dto.user.register.request.ManagerRegiDTO;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.WorkExperience;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.caregiver.WorkExperienceRepository;
import com.five.Maeum_Eum.repository.center.CenterRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
import com.five.Maeum_Eum.service.center.KakaoAddressService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterService {
    private final ManagerRepository managerRepository;
    private final CaregiverRepository caregiverRepository;
    private final CenterRepository centerRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final BCryptPasswordEncoder encoder;
    private final KakaoAddressService kakaoAddressService;


    public ManagerBasicDto registerManager(ManagerRegiDTO regiDTO) {

        if (managerRepository.existsByLoginId(regiDTO.getId()) || caregiverRepository.existsByLoginId(regiDTO.getId())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        for (ConstraintViolation<ManagerRegiDTO> violation : Validation.buildDefaultValidatorFactory().getValidator().validate(regiDTO)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 센터 조회 후 차량 보유 여부 변경
        Center center = centerRepository.findByDetailAddress(regiDTO.getAddress()).orElse(null);
        if (center == null) { throw new CustomException(ErrorCode.CENTER_NOT_FOUND);
        }
        center.registerManager(regiDTO.getHasCar());
        centerRepository.save(center);

        Manager manager = Manager.builder()
                .loginId(regiDTO.getId())
                .name(regiDTO.getName())
                .password(encoder.encode(regiDTO.getPassword()))
                .center(center)
                .phoneNumber(regiDTO.getPhone())
                .build();

        managerRepository.save(manager);

        ManagerBasicDto managerBasicDto = ManagerBasicDto.of(manager , center);

        return managerBasicDto;
    }

    public boolean registerCaregiver(CaregiverRegiDTO regiDTO) {

        if (managerRepository.existsByLoginId(regiDTO.getId()) || caregiverRepository.existsByLoginId(regiDTO.getId())) {
            return false;
        }
        for (ConstraintViolation<CaregiverRegiDTO> violation : Validation.buildDefaultValidatorFactory().getValidator().validate(regiDTO)) {
            return false;
        }

        Caregiver caregiver = Caregiver.builder()
                .name(regiDTO.getName())
                .loginId(regiDTO.getId())
                .introduction(regiDTO.getIntroduction())
                .password(encoder.encode(regiDTO.getPassword()))
                .phoneNumber(regiDTO.getPhone())
                .address(regiDTO.getAddress())
                .location(kakaoAddressService.getCoordinates(regiDTO.getAddress()))
                .jobState(Caregiver.JobState.IDLE)
                .isJobOpen(false)
                .hasCaregiverCertificate(false)
                .build();

        caregiverRepository.save(caregiver);

        // 경력 사항 추출 및 저장
        for (ExperienceDTO expDTO : regiDTO.getExperience()) {
            Center center = centerRepository.findByDetailAddress(expDTO.getCenter()).orElse(null);

            WorkExperience workExperience = WorkExperience.builder()
                    .caregiver(caregiver)
                    .startDate(expDTO.getStartDate())
                    .endDate(expDTO.getEndDate())
                    .center(center)
                    .build();

            workExperienceRepository.save(workExperience);
        }


        return true;
    }

    public boolean validateDuplicateID(String id) {
        return managerRepository.existsByLoginId(id) | caregiverRepository.existsByLoginId(id);
    }
}
