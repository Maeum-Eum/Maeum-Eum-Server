package com.five.Maeum_Eum.entity.user.caregiver;

import com.five.Maeum_Eum.common.BaseTimeEntity;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apply")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Apply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long applyId;

    @ManyToOne
    @JoinColumn(name = "elder_id", nullable = false)
    private Elder elder;

    @ManyToOne
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Column(length = 1024)
    private String messageFromManager; // 관리자 -> 요양보호소

    @Column(length = 1024)
    private String messageFromCaregiver; // 관리자 -> 요양보호소

    @Column
    private String workRequirement;

    @Column(length = 20)
    private String managerPhoneNumber; // 관리자 핸드폰

    @Column(length = 20)
    private String caregiverPhoneNumber; // 요양복지사 핸드폰

    public void updateStatus(ApprovalStatus approvalStatus){
        this.approvalStatus = approvalStatus;
    }
}
