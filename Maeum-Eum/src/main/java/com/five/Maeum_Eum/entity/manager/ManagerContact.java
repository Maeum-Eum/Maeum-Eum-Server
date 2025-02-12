package com.five.Maeum_Eum.entity.manager;

import com.five.Maeum_Eum.common.BaseTimeEntity;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerContact extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @ManyToOne
    @JoinColumn(name = "caregiver_id")
    private Caregiver caregiver;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Builder
    public ManagerContact(ApprovalStatus approvalStatus){
        this.approvalStatus = approvalStatus;
    }

}
