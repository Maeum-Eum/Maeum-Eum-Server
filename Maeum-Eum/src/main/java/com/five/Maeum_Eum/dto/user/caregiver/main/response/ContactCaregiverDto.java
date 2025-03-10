package com.five.Maeum_Eum.dto.user.caregiver.main.response;

import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;

import java.util.List;

/*제목 , 협의 여부 , updateAt , 센터 이름 , 시급 , 일상 , 이동, 식사?*/
public record ContactCaregiverDto(

        Long managerContactId,
        ApprovalStatus approvalStatus,
        Long caregiverId,
        String title ,
        boolean negotiable,
        Long centerId,
        String centerName ,
        int wage ,
        String address ,
        List<String> possibleTasks

) {

    public static ContactCaregiverDto from(ManagerContact managerContact , String title , List<String> possibleTasks){
        return new ContactCaregiverDto(
                managerContact.getContactId(),
                managerContact.getApprovalStatus(),
                managerContact.getCaregiver().getCaregiverId(),
                title,
                managerContact.getCaregiver().getResume().getNegotiableTime(),
                managerContact.getManager().getCenter().getCenterId(),
                managerContact.getManager().getCenter().getCenterName(),
                managerContact.getWage(),
                managerContact.getCaregiver().getAddress(),
                possibleTasks
        );
    }
}
