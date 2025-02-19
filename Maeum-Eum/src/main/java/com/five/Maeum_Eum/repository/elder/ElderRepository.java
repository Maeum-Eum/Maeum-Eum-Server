package com.five.Maeum_Eum.repository.elder;

import com.five.Maeum_Eum.entity.user.elder.Elder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElderRepository extends JpaRepository<Elder, Long> {
    long countByElderName(String elderName);
    Optional<Elder> findByElderName(String elderName);

    @Query("select e from Elder e where e.manager.managerId =:managerId")
    List<Elder> findByManagerId(@Param("managerId") Long managerId);
}
