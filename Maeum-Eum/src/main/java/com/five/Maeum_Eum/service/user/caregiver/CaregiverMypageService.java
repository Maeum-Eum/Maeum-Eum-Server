package com.five.Maeum_Eum.service.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.mypage.CaregiverMypageDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CaregiverMypageService {

    public CaregiverMypageDTO getDto(Caregiver findCaregiver) {

        CaregiverMypageDTO dto = CaregiverMypageDTO.builder()
                .name(findCaregiver.getName())
                .address(findCaregiver.getAddress())
                .savedEldersCount(findCaregiver.getSavedElders().size())
                .managerContactCount(findCaregiver.getManagerContact().size())
                .applyCount(findCaregiver.getApplys().size())
                .isJobOpen(findCaregiver.isJobOpen())
                .ProfileImage(findCaregiver.getResume() != null ? findCaregiver.getResume().getProfileImage() : "")
                .isResumeRegistered(findCaregiver.isResumeRegistered())
                .build();

        return dto;
    }
}
