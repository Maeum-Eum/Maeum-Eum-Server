package com.five.Maeum_Eum.controller.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.resume.request.ResumeSaveDTO;
import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ExperienceDTO;
import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ResumeResponseDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Certificate;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.entity.user.caregiver.WorkExperience;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/caregiver/resume")
public class ResumeController {

    private final CaregiverService caregiverService;
    private final JWTUtil jwtUtil;
    private final WorkExperienceService workExperienceService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<Object> resume(@RequestHeader("Authorization") String authHeader) {


        String token = authHeader.substring(7).trim();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
        }

        if(!findCaregiver.isResumeRegistered()) {
            throw new CustomException(ErrorCode.RESUME_NOT_REGISTERED, "이력서가 존재하지 않는 유저입니다.");
        }

        Resume resume = findCaregiver.getResume();

        // 월급 계산 로직 적용
        int salary = 50000;

        List<ExperienceDTO> experienceDTOList = new ArrayList<>();
        for(WorkExperience experience : findCaregiver.getExperience()){

            ExperienceDTO dto = ExperienceDTO.builder()
                    .startDate(experience.getStartDate())
                    .endDate(experience.getEndDate())
                    .work(experience.getWork())
                    .centerId(experience.getCenter() != null ? experience.getCenter().getCenterId().toString() : null)
                    .build();

            experienceDTOList.add(dto);
        }

        ResumeResponseDTO responseDTO = ResumeResponseDTO.builder()
                .jobPosition(resume.getJobPosition())
                .certificateCode(resume.getCertificate().getCertificateCode())
                .hasDementiaTraining(resume.getHasDementiaTraining())
                .hasVehicle(resume.getHasVehicle())
                .workPlace(resume.getWorkPlace())
                .workDay(resume.getWorkDay())
                .workTimeSlot(resume.getWorkTimeSlot())
                .isNegotiableTime(resume.getNegotiableTime())
                .wage(resume.getWage())
                .expectedSalary(salary)
                .elderRank(resume.getElderRank())
                .meal(resume.getMeal())
                .toileting(resume.getToileting())
                .mobility(resume.getMobility())
                .daily(resume.getDaily())
                .preferredGender(resume.getPreferredGender())
                .isFamilyPreferred(resume.getFamilyPreferred())
                .experience(experienceDTOList)
                .introduction(resume.getIntroduction())
                .profileImage(resume.getProfileImage())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_CAREGIVER')")
    public ResponseEntity<Object> saveResume(@RequestHeader("Authorization") String authHeader, @RequestBody ResumeSaveDTO resumeSaveDTO) {

        String token = authHeader.substring(7).trim();
        Caregiver findCaregiver = caregiverService.findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
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
                .profileImage(resumeSaveDTO.getProfileImage())
                .toileting(resumeSaveDTO.getToileting())
                .wage(resumeSaveDTO.getWage())
                .workPlace(resumeSaveDTO.getWorkPlace())
                .workDay(resumeSaveDTO.getWorkDay())
                .workTimeSlot(resumeSaveDTO.getWorkTimeSlot())
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