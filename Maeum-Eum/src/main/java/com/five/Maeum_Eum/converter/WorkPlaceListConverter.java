package com.five.Maeum_Eum.converter;

import com.five.Maeum_Eum.entity.user.caregiver.WorkPlace;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WorkPlaceListConverter extends AbstractEnumListConverter<WorkPlace> {
    public WorkPlaceListConverter() {
        super(WorkPlace.class);
    }
}
