package com.five.Maeum_Eum.repository.caregiver;

import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    Optional<WorkExperience> findByCenter(Center center);
    boolean existsByCenterAndAndCaregiverAndStartDate(Center center, Caregiver caregiver, LocalDate startDate);
}
