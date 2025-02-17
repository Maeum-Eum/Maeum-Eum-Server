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
    private int wage; // 시급만

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
    public ManagerContact(ApprovalStatus approvalStatus , int wage , String messageFromManager, String messageFromCaregiver,String workRequirement  , String managerPhoneNumber , boolean negotiable){
        this.approvalStatus = approvalStatus;
        this.wage = wage;
        this.messageFromManager = messageFromManager;
        this.messageFromCaregiver = messageFromCaregiver;
        this.workRequirement = workRequirement;
        this.managerPhoneNumber = managerPhoneNumber;
        this.negotiable = negotiable;
    }


    public void approve(String message, String phone){
        approvalStatus = ApprovalStatus.APPROVED;
        messageFromManager = message;
        caregiverPhoneNumber = phone;
    }

    public void reject() {
        approvalStatus = ApprovalStatus.REJECTED;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setCaregiver(Caregiver caregiver) {
        this.caregiver = caregiver;
    }

    public void setElder(Elder elder) {
        this.elder = elder;
    }

}
