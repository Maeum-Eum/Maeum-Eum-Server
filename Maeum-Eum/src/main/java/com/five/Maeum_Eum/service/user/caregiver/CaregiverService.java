package com.five.Maeum_Eum.service.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.resume.request.ResumeSaveDTO;
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

    public void registerResume(Caregiver caregiver, Resume resume) {
        caregiver.setResumeRegistered(true);
        System.out.println("registerResume 가족 선호도 상태 : " + resume.getFamilyPreferred());
        System.out.println("registerResume 반려 선호도 상태 : " + resume.getPetPreferred());
        System.out.println("registerResume 시급 네고 선호도 상태 : " + resume.getNegotiableTime());
        resumeRepository.save(resume);
    }

    public void toggleJobOpenState(Caregiver caregiver) {
        caregiver.toggleJobOpenState();
    }

    public void updateResume(Resume resume, ResumeSaveDTO dto) {
        resume.updateResume(dto);
    }
}
