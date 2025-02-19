package com.five.Maeum_Eum.service.user.caregiver;

import com.five.Maeum_Eum.common.PageResponse;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.SimpleContactDTO;
import com.five.Maeum_Eum.dto.user.caregiver.resume.request.ResumeSaveDTO;
import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ExperienceDTO;
import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ResumeResponseDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Apply;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.entity.user.caregiver.WorkExperience;
import com.five.Maeum_Eum.entity.user.elder.DayOfWeek;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.repository.caregiver.ApplyQueryDsl;
import com.five.Maeum_Eum.repository.caregiver.ApplyRepository;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.caregiver.ResumeRepository;
import com.five.Maeum_Eum.repository.elder.ElderQueryDsl;
import com.five.Maeum_Eum.repository.manager.ManagerContactQueryDsl;
import com.five.Maeum_Eum.repository.manager.ManagerContactRepository;
import com.five.Maeum_Eum.service.aws.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class CaregiverService {

    private final CaregiverRepository caregiverRepository;
    private final ResumeRepository resumeRepository;
    private final S3Uploader s3Uploader;
    private final JWTUtil jwtUtil;
    private final CaregiverMainService caregiverMainService;
    private final ManagerContactQueryDsl managerContactQueryDsl;
    private final ApplyQueryDsl applyQueryDsl;
    private final ElderQueryDsl elderQueryDsl;


    public Caregiver findCaregiverByLoginId(String loginId) {

        return caregiverRepository.findByLoginId(loginId).orElse(null);
    }

    public void registerResume(Caregiver caregiver, Resume resume) {
        caregiver.setResumeRegistered(true);
        resumeRepository.save(resume);
    }

    // token으로  사용자 role 알아내기
    private String findRole(String token){
        return jwtUtil.getRole(token);
    }
    public void toggleJobOpenState(Caregiver caregiver) {
        caregiver.toggleJobOpenState();
    }

    public void updateResume(Resume resume, ResumeSaveDTO dto) {
        resume.updateResume(dto);
    }

    public String uploadProfileImage(MultipartFile file, Caregiver caregiver) throws IllegalAccessException {

        Resume resume;
        if(!caregiver.isResumeRegistered()) {
            // 프로필 사진 이미지만 포함한 이력서로 제작
            String ProfileUrl = s3Uploader.uploadImage(file);
            System.out.println("진짜 로그 : " + ProfileUrl);
            resume = Resume.builder()
                    .caregiver(caregiver)
                    .profileImage(ProfileUrl)
                    .build();
            resumeRepository.save(resume);
            return "프로필 사진 업로드에 성공하였습니다.";
        }

        // 이력서 이미 존재 - 이미지 대체 하기
        resume = caregiver.getResume();
        String ProfileUrl = s3Uploader.uploadImage(file);
        resume.setProfileImage(ProfileUrl);
        return "프로필 사진 업로드에 성공하였습니다.";
    }

    public String deleteProfileImage(Caregiver caregiver) throws CustomException {

        // 이력서가 없다면 오류
        Resume resume = caregiver.getResume();

        if(resume == null) {
            throw new CustomException(ErrorCode.RESUME_NOT_REGISTERED, "이력서가 없습니다.");
        }
        System.out.println("[LOG] 제거하려는 파일 : "+ resume.getProfileImage());
        String s3Url = resume.getProfileImage();
        s3Uploader.fileDelete(s3Url);
        resume.setProfileImage("");
        return "프로필 사진 제거 성공";
    }


    /*요양보호사가 자신의 이력서 조회 */
    public ResumeResponseDTO getMyResume(String token){

        Caregiver findCaregiver = findCaregiverByLoginId(jwtUtil.getId(token));
        if(findCaregiver == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "유저 정보를 가져오지 못했습니다.");
        }

        if(!findCaregiver.isResumeRegistered()) {
            throw new CustomException(ErrorCode.RESUME_NOT_REGISTERED, "이력서가 존재하지 않는 유저입니다.");
        }

        Resume resume = findCaregiver.getResume();

        ResumeResponseDTO responseDTO = makeResponse(findCaregiver , resume);

        return responseDTO;
    }

    /* 관리자가 특정 요양 보호사의 이력서 조회 */
    public  ResumeResponseDTO getCaregiverResume(String token, Long caregiverId) {
        if(!findRole(token).equals("ROLE_MANAGER")){ // 사용자가 관리자 역할이 아닐 때
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Resume resume = resumeRepository.findByCaregiverId(caregiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_REGISTERED));

        ResumeResponseDTO responseDTO = makeResponse(caregiver , resume);

        return responseDTO;

    }

    public PageResponse<SimpleContactDTO> getContacts(Pageable pageable, ApprovalStatus approvalStatus) {
        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Page<SimpleContactDTO> page = managerContactQueryDsl.findMyContacts(caregiver,pageable, approvalStatus);

        // 엔티티를 DTO로 변환
        List<SimpleContactDTO> contents = page.getContent()
                .stream().map(caregiverMainService::toDTO)
                .toList();

        return PageResponse.<SimpleContactDTO>builder()
                .first(page.isFirst())
                .last(page.isLast())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(contents)
                .build();

    }

    public PageResponse<SimpleContactDTO> getApplies(Pageable pageable, ApprovalStatus approvalStatus) {
        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Page<SimpleContactDTO> page = applyQueryDsl.findMyApplies(caregiver,pageable, approvalStatus);

        // 엔티티를 DTO로 변환
        List<SimpleContactDTO> contents = page.getContent()
                .stream().map(caregiverMainService::toDTOApply)
                .toList();

        return PageResponse.<SimpleContactDTO>builder()
                .first(page.isFirst())
                .last(page.isLast())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(contents)
                .build();
    }

    public PageResponse<SimpleContactDTO> getBookmark(Pageable pageable) {
        String caregiverId = SecurityContextHolder.getContext().getAuthentication().getName();
        Caregiver caregiver = caregiverRepository.findByLoginId(caregiverId).orElse(null);
        if (caregiver == null) { throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Page<SimpleContactDTO> page = elderQueryDsl.findMyBookmark(caregiver,pageable);

        // 엔티티를 DTO로 변환
        List<SimpleContactDTO> contents = page.getContent()
                .stream().map(caregiverMainService::toDTOBookmark)
                .toList();

        return PageResponse.<SimpleContactDTO>builder()
                .first(page.isFirst())
                .last(page.isLast())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(contents)
                .build();
    }

    /* 이력서 제목 만들기 */
    public String makeTitle(Resume resume){
        List<String> workDay = new ArrayList<>();

        List<Integer> workDayDto = resume.getWorkDay();
        for (int idx : workDayDto) {
            workDay.add(DayOfWeek.values()[idx].toString());
        }

        String middleWorkDay = workDay.stream()
                .collect(Collectors.joining("/"));

        String position = resume.getJobPosition().get(0); // Job position

        List<String> combinedList = new ArrayList<>();
        combinedList.addAll(resume.getToileting());
        combinedList.addAll(resume.getMeal());
        combinedList.addAll(resume.getDaily());
        combinedList.addAll(resume.getMobility());

        Collections.shuffle(combinedList); // 랜덤하게 섞기

        List<String> randomTwo = combinedList.subList(0, 2);

        String randomTwoString = String.join(" ", randomTwo);

        String result = "[" + middleWorkDay + "]" + position + "- " + randomTwoString;
        return result;
    }


    /* 이력서 응답 DTO 만들기 */
    private ResumeResponseDTO makeResponse(Caregiver caregiver , Resume resume){

        List<ExperienceDTO> experienceDTOList = new ArrayList<>();
        for(WorkExperience experience : caregiver.getExperience()){

            ExperienceDTO dto = ExperienceDTO.builder()
                    .startDate(experience.getStartDate())
                    .endDate(experience.getEndDate())
                    .work(experience.getWork())
                    .centerId(experience.getCenter() != null ? experience.getCenter().getCenterId().toString() : null)
                    .build();

            experienceDTOList.add(dto);
        }

        String title = makeTitle(resume);

        ResumeResponseDTO responseDTO = ResumeResponseDTO.builder()
                .caregiverId(caregiver.getCaregiverId())
                .resumeId(resume.getResumeId())
                .title(title)
                .jobPosition(resume.getJobPosition())
                .certificateCode(resume.getCertificate().getCertificateCode())
                .hasDementiaTraining(resume.getHasDementiaTraining())
                .hasVehicle(resume.getHasVehicle())
                .workPlace(resume.getWorkPlace())
                .workDay(resume.getWorkDay())
                .workTimeSlot(resume.getWorkTimeSlot())
                .isNegotiableTime(resume.getNegotiableTime())
                .wage(resume.getWage())
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

        return responseDTO;
    }

}
