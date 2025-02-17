package com.five.Maeum_Eum.entity.user.manager;

import com.five.Maeum_Eum.common.BaseTimeEntity;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private int wage; // 건 별인지 , 시급인지 , 월급 , 일급인지에 따라 다름

    @Column(length = 1024)
    private String messageFromManager; // 관리자 -> 요양보호소

    @Column(length = 1024)
    private String messageFromCaregiver; // 관리자 -> 요양보호소

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String workRequirement;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WageType wageType;

    @Column(length = 20)
    private String managerPhoneNumber; // 관리자 핸드폰

    @Column(length = 20)
    private String caregiverPhoneNumber; // 요양복지사 핸드폰

    @ManyToOne
    @JoinColumn(name = "caregiver_id")
    private Caregiver caregiver;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "elder_id")
    private Elder elder;

    private boolean negotiable;

    @Builder
    public ManagerContact(ApprovalStatus approvalStatus , int wage , String messageFromManager, String workRequirement , WageType wageType , String managerPhoneNumber){
        this.approvalStatus = approvalStatus;
        this.wage = wage;
        this.messageFromManager = messageFromManager;
        this.workRequirement = workRequirement;
        this.wageType = wageType;
        this.managerPhoneNumber = managerPhoneNumber;
    }

    public enum WageType{
        HOURLY,   // 시급
        SALARY,   // 월급
        PER_CASE, // 건 당
        DAILY // 일급
    }

    public void approve(String message, String phone){
        approvalStatus = ApprovalStatus.APPROVED;
        messageFromManager = message;
        caregiverPhoneNumber = phone;
    }

    public void reject() {
        approvalStatus = ApprovalStatus.REJECTED;
    }

}
