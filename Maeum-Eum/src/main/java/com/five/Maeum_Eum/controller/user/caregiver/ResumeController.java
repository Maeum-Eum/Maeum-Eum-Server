package com.five.Maeum_Eum.controller.user.caregiver;

import com.five.Maeum_Eum.common.WorkCalculator;
import com.five.Maeum_Eum.controller.work.DailyType;
import com.five.Maeum_Eum.controller.work.MealType;
import com.five.Maeum_Eum.controller.work.MobilityType;
import com.five.Maeum_Eum.controller.work.ToiletingType;
import com.five.Maeum_Eum.dto.user.caregiver.resume.request.ResumeSaveDTO;
import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ResumeResponseDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Certificate;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.exception.ErrorResponse;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import com.five.Maeum_Eum.service.user.caregiver.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/caregiver/resume")
public class ResumeController {

    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;
    private final WorkExperienceService workExperienceService;

    @GetMapping("/salary")
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<Object> salary(@RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7).trim();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
        }
        Resume resume = findCaregiver.getResume();

        Map<String,Integer> response = new HashMap<>();
        response.put("salary", WorkCalculator.calculateSalary(WorkCalculator.getWorkDayTime(resume.getWorkTimeSlot()) * resume.getWorkDay().size(), resume.getWage()));
        return ResponseEntity.ok(response);
    }


    /* 요양보호사가 자신의 이력서 조회 */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<Object> resume(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7).trim();

        ResumeResponseDTO responseDTO = caregiverService.getMyResume(token);

        return ResponseEntity.ok(responseDTO);
    }

    /* 요양보호사 이력서 저장 */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<Object> saveResume(@RequestHeader("Authorization") String authHeader, @RequestBody ResumeSaveDTO resumeSaveDTO) {

        String token = authHeader.substring(7).trim();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
        }

        for(String test : resumeSaveDTO.getDaily()){
            DailyType dailyType = DailyType.fromLabel(test);
        }

        if(findCaregiver.isResumeRegistered())
        {
            caregiverService.updateResume(findCaregiver.getResume(), resumeSaveDTO);
            workExperienceService.saveAll(findCaregiver, resumeSaveDTO);
            return ResponseEntity
                    .status(200)
                    .body(ErrorResponse.builder()
                            .code("Resume Modified Successfully")
                            .status(HttpStatus.OK)
                            .message("이력서 수정 완료")
                            .build());
        }

        Certificate certificate = Certificate.builder()
                .certificateCode(resumeSaveDTO.getCertificateCode())
                .certificateRank(1)
                .certificateType(Certificate.CertificateType.CARE_GIVER)
                .build();

        // 이력서 저장
        Resume resume = Resume.builder()
                .certificate(certificate)
                .caregiver(findCaregiver)
                .daily(resumeSaveDTO.getDaily())
                .elderRank(resumeSaveDTO.getElderRank())
                .hasDementiaTraining(resumeSaveDTO.getHasDementiaTraining())
                .hasVehicle(resumeSaveDTO.getHasVehicle())
                .introduction(resumeSaveDTO.getIntroduction())
                .petPreferred(resumeSaveDTO.getIsPetPreferred())
                .familyPreferred(resumeSaveDTO.getIsFamilyPreferred())
                .negotiableTime(resumeSaveDTO.getIsNegotiableTime())
                .jobPosition(resumeSaveDTO.getJobPosition())
                .meal(resumeSaveDTO.getMeal())
                .mobility(resumeSaveDTO.getMobility())
                .preferredGender(resumeSaveDTO.getPreferredGender())
                .toileting(resumeSaveDTO.getToileting())
                .wage(resumeSaveDTO.getWage())
                .workPlace(resumeSaveDTO.getWorkPlace())
                .workDay(resumeSaveDTO.getWorkDay())
                .workTimeSlot(resumeSaveDTO.getWorkTimeSlot())

                // 4개의 등급 수준 결정
                .mealLevel(
                        resumeSaveDTO.getMeal().stream()
                                .map(MealType::fromLabel)
                                .mapToInt(MealType::getLevel)
                                .max()
                                .orElse(0)
                )
                .dailyFilter1(
                        resumeSaveDTO.getDaily().contains(DailyType.ONE.getLabel())
                )
                .dailyFilter2(
                        resumeSaveDTO.getDaily().contains(DailyType.TWO.getLabel())
                )
                .dailyFilter3(
                        resumeSaveDTO.getDaily().contains(DailyType.THREE.getLabel())
                )
                .dailyFilter4(
                        resumeSaveDTO.getDaily().contains(DailyType.FOUR.getLabel())
                )
                .dailyFilter5(
                        resumeSaveDTO.getDaily().contains(DailyType.FIVE.getLabel())
                )
                .dailyFilter6(
                        resumeSaveDTO.getDaily().contains(DailyType.SIX.getLabel())
                )
                .mobilityLevel(
                        resumeSaveDTO.getMobility().stream()
                                .map(MobilityType::fromLabel)
                                .mapToInt(MobilityType::getLevel)
                                .max()
                                .orElse(0)

                )
                .toiletingLevel(
                        resumeSaveDTO.getToileting().stream()
                                .map(ToiletingType::fromLabel)
                                .mapToInt(ToiletingType::getLevel)
                                .max()
                                .orElse(0)
                )

                // 인지 지원 가능 등급 결정
                .elderRankLevel(
                        resumeSaveDTO.getElderRank().stream()
                                .mapToInt(Integer::intValue)
                                .max()
                                .orElse(0)
                )

                .build();

        workExperienceService.saveAll(findCaregiver, resumeSaveDTO);
        caregiverService.registerResume(findCaregiver, resume);

        return ResponseEntity
                .status(200)
                .body(ErrorResponse.builder()
                        .code("Resume Registered Successfully")
                        .status(HttpStatus.OK)
                        .message("이력서 저장 완료")
                        .build());
    }
}