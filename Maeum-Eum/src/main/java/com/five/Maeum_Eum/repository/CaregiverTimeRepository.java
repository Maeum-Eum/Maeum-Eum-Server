package com.five.Maeum_Eum.repository;

import com.five.Maeum_Eum.entity.CaregiverTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaregiverTimeRepository extends JpaRepository<CaregiverTime, Long> {
}
