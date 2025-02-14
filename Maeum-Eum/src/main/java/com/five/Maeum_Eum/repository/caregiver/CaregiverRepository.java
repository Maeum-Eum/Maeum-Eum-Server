package com.five.Maeum_Eum.repository.caregiver;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
}
