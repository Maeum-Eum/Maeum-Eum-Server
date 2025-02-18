package com.five.Maeum_Eum.repository.caregiver;

import com.five.Maeum_Eum.entity.user.caregiver.Apply;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyRepository extends JpaRepository<Apply,Long> {
    Page<Apply> findAllByCaregiverAndApprovalStatus(Pageable pageable, Caregiver caregiver
            , ApprovalStatus approvalStatus);
    boolean existsByCaregiverAndElder(Caregiver caregiver, Elder elder);
}
