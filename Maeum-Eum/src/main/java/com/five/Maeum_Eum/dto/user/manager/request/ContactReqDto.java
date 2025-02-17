package com.five.Maeum_Eum.dto.user.manager.request;


import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContactReqDto {

    Long elderId;
    String workRequirement;
    int wage;
    JsonNullable<String> phoneNumber;
    String message;

    public ContactReqDto(Long elderId , String workRequirement , int wage , JsonNullable<String>  phoneNumber , String message){
        this.elderId = elderId;
        this.workRequirement = workRequirement;
        this.wage = wage;
        this.phoneNumber = phoneNumber;
        this.message = message;
    }


    public static ManagerContact toEntity(Manager manager , Caregiver caregiver , Elder elder , ContactReqDto dto){
        ManagerContact contact = ManagerContact.builder()
                .approvalStatus(ApprovalStatus.PENDING)
                .managerPhoneNumber(dto.getPhoneNumber().isPresent() ? dto.getPhoneNumber().get() : manager.getPhoneNumber())
                .messageFromManager(dto.getMessage())
                .messageFromManager(dto.getMessage())
                .wage(dto.getWage())
                .workRequirement(dto.getWorkRequirement())
                .negotiable(elder.isNegotiable()) // 기본적으로 협상 불가능
                .build();

        contact.setManager(manager);
        contact.setCaregiver(caregiver);
        contact.setElder(elder);

        return contact;
    }



}
