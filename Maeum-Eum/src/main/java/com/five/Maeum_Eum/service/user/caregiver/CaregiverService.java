package com.five.Maeum_Eum.service.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.mypage.CaregiverMypageDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.caregiver.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CaregiverService {

    private final CaregiverRepository caregiverRepository;
    private final ResumeRepository resumeRepository;

    public Caregiver findCaregiverByLoginId(String loginId) {

        return caregiverRepository.findByLoginId(loginId).orElse(null);
    }

    public void registerResume(Resume resume) {

        resumeRepository.save(resume);
    }
}
