package com.five.Maeum_Eum.dto.manager;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CaregiverWithOverlapDto {
    private Caregiver caregiver;
    private Integer totalOverlap;
}
