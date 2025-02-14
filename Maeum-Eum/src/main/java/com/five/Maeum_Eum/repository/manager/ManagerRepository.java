package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    boolean existsByLoginId(String loginId);
    Optional<Manager> findByLoginId(String loginId);
}
