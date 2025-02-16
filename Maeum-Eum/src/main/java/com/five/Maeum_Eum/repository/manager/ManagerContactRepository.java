package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerContactRepository extends JpaRepository<ManagerContact, Long> {

    @Query("SELECT COUNT(mc) FROM ManagerContact mc WHERE mc.manager.managerId = :managerId")
    int countManagerContactByManagerId(@Param("managerId") Long managerId);
}

