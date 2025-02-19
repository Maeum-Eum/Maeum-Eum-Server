package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerContactRepository extends JpaRepository<ManagerContact, Long> {

    @Query("SELECT COUNT(mc) FROM ManagerContact mc WHERE mc.manager.managerId = :managerId")
    int countManagerContactByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT mc FROM ManagerContact mc WHERE mc.approvalStatus =:state AND mc.manager.managerId =:managerId AND mc.elder.elderId =:elderId")
    List<ManagerContact> findByApprovalStatusAndManagerIdAndElderId(@Param("managerId") Long managerId, @Param("elderId") Long elderId ,@Param("state") ApprovalStatus state);

    Page<ManagerContact> findAllByCaregiverAndApprovalStatus(Pageable pageable, Caregiver caregiver
            , ApprovalStatus approvalStatus);
}

