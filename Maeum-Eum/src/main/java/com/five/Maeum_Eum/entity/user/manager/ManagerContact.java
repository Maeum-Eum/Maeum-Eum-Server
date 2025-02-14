package com.five.Maeum_Eum.entity.user.manager;

import com.five.Maeum_Eum.common.BaseTimeEntity;
import com.five.Maeum_Eum.converter.GenericListConverter;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private int wage; // 건 별인지 , 시급인지 , 월급인지에 따라 다름

    @Column(length = 1024)
    private String message; // 관리자 -> 요양보호소

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> workRequirement;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WageType wageType;

    @Column(length = 20)
    private String phoneNumber;

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
    public ManagerContact(ApprovalStatus approvalStatus , int wage , String message , List<String> workRequirement , WageType wageType , String phoneNumber){
        this.approvalStatus = approvalStatus;
        this.wage = wage;
        this.message = message;
        this.workRequirement = workRequirement;
        this.wageType = wageType;
        this.phoneNumber = phoneNumber;
    }

    public enum WageType{
        HOURLY,   // 시급
        SALARY,   // 월급
        PER_CASE // 건 당
    }

}
