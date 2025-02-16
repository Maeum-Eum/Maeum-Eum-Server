package com.five.Maeum_Eum.service.matching;

import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class matchingService {

    private final CaregiverService caregiverService;
}