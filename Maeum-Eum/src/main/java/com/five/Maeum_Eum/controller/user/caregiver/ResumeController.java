package com.five.Maeum_Eum.controller.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.resume.request.ResumeSaveDTO;
import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ResumeResponseDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.exception.ErrorResponse;
import com.five.Maeum_Eum.jwt.CaregiverUserDetails;
import com.five.Maeum_Eum.service.user.caregiver.CaregiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/caregiver")
public class ResumeController {

    private final CaregiverService caregiverService;

    @GetMapping("/resume")
    public ResponseEntity<Object> resume(@AuthenticationPrincipal CaregiverUserDetails userDetails) {

        String caregiverId = userDetails.getUsername();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(caregiverId);

        Resume resume = findCaregiver.getResume();

        if(resume == null) {
            return ResponseEntity
                    .status(400)
                    .body(ErrorResponse.builder()
                            .code("Resume Not Found")
                            .status(HttpStatus.BAD_REQUEST)
                            .message("이력서 정보를 가져오지 못했습니다.")
                            .build());
        }

        // 월급 계산 로직

        int salary = 50000;

        ResumeResponseDTO responseDTO = ResumeResponseDTO.builder()
                .jobPosition(resume.getJobPosition())
                .certificateCode(resume.getCertificate().getCertificateCode())
                .hasDementiaTraining(resume.getHasDementiaTraining())
                .hasVehicle(resume.isHasVehicle())
                .workPlace(resume.getWorkPlace())
                .workDay(resume.getWorkDay())
                .workTimeSlot(resume.getWorkTimeSlot())
                .isNegotiableTime(resume.isNegotiableTime())
                .wage(resume.getWage())
                .expectedSalary(salary)
                .elderRank(resume.getElderRank())
                .meal(resume.getMeal())
                .toileting(resume.getToileting())
                .mobility(resume.getMobility())
                .daily(resume.getDaily())
                .preferredGender(resume.getPreferredGender())
                .isFamilyPreferred(resume.isFamilyPreferred())
                //.experience(findCaregiver.get)
                .introduction(resume.getIntroduction())
                .profileImage(resume.getProfileImage())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/resume/create")
    public ResponseEntity<Object> saveResume(@AuthenticationPrincipal CaregiverUserDetails userDetails, @RequestBody ResumeSaveDTO resumeSaveDTO){

        String caregiverId = userDetails.getUsername();
        Resume resume = caregiverService.findCaregiverByLoginId(caregiverId).getResume();

        caregiverService.registerResume(resume);
        return ResponseEntity.ok(resumeSaveDTO);
    }

//    @PostMapping("/resume/create")
//    public ResponseEntity<Object> saveResume(@AuthenticationPrincipal CaregiverUserDetails userDetails, @RequestBody ResumeSaveDTO resumeSaveDTO){
//
//        String caregiverId = userDetails.getUsername();
//        Resume resume = caregiverService.findCaregiverByLoginId(caregiverId).getResume();
//        caregiverService.registerResume(resume);
//
//        return ResponseEntity.ok(null);
//    }
}