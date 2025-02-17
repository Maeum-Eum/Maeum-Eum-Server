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
