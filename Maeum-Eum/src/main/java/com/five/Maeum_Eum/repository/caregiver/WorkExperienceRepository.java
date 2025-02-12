package com.five.Maeum_Eum.repository.caregiver;

import com.five.Maeum_Eum.entity.user.caregiver.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
}
