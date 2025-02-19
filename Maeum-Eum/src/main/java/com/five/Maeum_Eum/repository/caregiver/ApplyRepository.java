package com.five.Maeum_Eum.repository.caregiver;

import com.five.Maeum_Eum.entity.user.caregiver.Apply;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply,Long> {
    Page<Apply> findAllByCaregiverAndApprovalStatus(Pageable pageable, Caregiver caregiver
            , ApprovalStatus approvalStatus);
    boolean existsByCaregiverAndElder(Caregiver caregiver, Elder elder);
    Optional<Apply> findByCaregiverAndElder(Caregiver caregiver, Elder elder);

    @Query("select a from Apply a where a.elder.elderId =:elderId  and a.approvalStatus =:status")
    List<Apply> findAllByElderAndApprovalStatus( @Param("elderId")Long elderId , @Param("status")ApprovalStatus approvalStatus);

    @Query("select a.caregiver from Apply a where a.applyId =:applyId")
    Caregiver findCaregiverByApplyId(@Param("applyId") Long applyId);

    @Query("SELECT COUNT(a) FROM Apply a WHERE a.elder.elderId = :elderId")
    int countByElderId(@Param("elderId") Long elderId);
}
