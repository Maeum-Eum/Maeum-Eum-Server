package com.five.Maeum_Eum.dto.user.manager.response;

import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;

public record ContactResDto(
        Long contactId,
        Long  caregiverId,
        Long elderId,
        String workRequirement,
        int wage,
        String phoneNumber,
        String messageFromManager
){
    /* 연락할 때 따로 폰 번호를 등록한 상태*/
    public static ContactResDto from(ManagerContact managerContact){
        return new ContactResDto(
                managerContact.getContactId(),
                managerContact.getCaregiver().getCaregiverId(),
                managerContact.getElder().getElderId(),
                managerContact.getWorkRequirement(),
                managerContact.getWage(),
                managerContact.getManagerPhoneNumber(),
                managerContact.getMessageFromManager()
        );
    }



}
