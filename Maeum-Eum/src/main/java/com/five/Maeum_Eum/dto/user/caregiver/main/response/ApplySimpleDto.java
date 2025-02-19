package com.five.Maeum_Eum.dto.user.caregiver.main.response;

import com.five.Maeum_Eum.entity.user.caregiver.Apply;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;

public record ApplySimpleDto(

        Long applyId,
        Long caregiverId,
        String caregiverName,
        Long elderId,
        String elderName,
        ApprovalStatus status

) {
    public static ApplySimpleDto from(Apply apply){
        return new ApplySimpleDto(
                apply.getApplyId(),
                apply.getCaregiver().getCaregiverId(),
                apply.getCaregiver().getName(),
                apply.getElder().getElderId(),
                apply.getElder().getElderName(),
                apply.getApprovalStatus()
        );
    }
}
