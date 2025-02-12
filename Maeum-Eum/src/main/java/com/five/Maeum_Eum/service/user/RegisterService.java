package com.five.Maeum_Eum.service.user;

import com.five.Maeum_Eum.dto.user.register.request.CaregiverRegiDTO;
import com.five.Maeum_Eum.dto.user.register.request.ManagerRegiDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
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
    private final BCryptPasswordEncoder encoder;

    public boolean registerManager(ManagerRegiDTO regiDTO) {

        if (managerRepository.existsByLoginId(regiDTO.getId()) || caregiverRepository.existsByLoginId(regiDTO.getId())) {
            return false;
        }
        for (ConstraintViolation<ManagerRegiDTO> violation : Validation.buildDefaultValidatorFactory().getValidator().validate(regiDTO)) {
            return false;
        }

        Manager manager = Manager.builder()
                .loginId(regiDTO.getId())
                .name(regiDTO.getName())
                .password(encoder.encode(regiDTO.getPassword()))
                .hasCar(regiDTO.getHasCar())
                .phoneNumber(regiDTO.getPhone())
                .build();

        managerRepository.save(manager);

        return true;
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
                .jobState(Caregiver.JobState.IDLE)
                .isJobOpen(false)
                .hasCaregiverCertificate(false)
                .build();

        caregiverRepository.save(caregiver);

        return true;
    }

    public boolean validateDuplicateID(String id) {
        return managerRepository.existsByLoginId(id) | caregiverRepository.existsByLoginId(id);
    }
}
