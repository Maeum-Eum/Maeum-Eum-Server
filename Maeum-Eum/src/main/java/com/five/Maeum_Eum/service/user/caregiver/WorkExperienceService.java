package com.five.Maeum_Eum.service.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.resume.request.ResumeSaveDTO;
import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ExperienceDTO;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.WorkExperience;
import com.five.Maeum_Eum.repository.caregiver.WorkExperienceRepository;
import com.five.Maeum_Eum.repository.center.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final CenterRepository centerRepository;

    public void saveAll(Caregiver caregiver, ResumeSaveDTO resumeSaveDTO) {

        List<WorkExperience> experienceList = new ArrayList<>();
        if (resumeSaveDTO.getExperience() != null) {
            for (ExperienceDTO dto : resumeSaveDTO.getExperience()) {
                Center center = centerRepository.findById(Long.parseLong(dto.getCenterId())).orElse(null);
                if(center == null) continue;

                WorkExperience exp = WorkExperience.builder()
                        .caregiver(caregiver)
                        .center(center)
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .work(dto.getWork())
                        .build();
                experienceList.add(exp);
            }
        }
        workExperienceRepository.saveAll(experienceList);
    }
}
