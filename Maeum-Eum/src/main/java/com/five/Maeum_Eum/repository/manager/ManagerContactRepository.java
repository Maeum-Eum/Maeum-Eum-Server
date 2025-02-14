package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerContactRepository extends JpaRepository<ManagerContact, Long> {
}

