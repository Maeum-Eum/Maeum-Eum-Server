package com.five.Maeum_Eum.repository.elder;

import com.five.Maeum_Eum.entity.user.elder.Elder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElderRepository extends JpaRepository<Elder, Long> {
    long countByElderName(String elderName);
}
