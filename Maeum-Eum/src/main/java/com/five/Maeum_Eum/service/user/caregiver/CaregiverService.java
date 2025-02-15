package com.five.Maeum_Eum.service.user.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.resume.request.ResumeSaveDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.caregiver.ResumeRepository;
import com.five.Maeum_Eum.service.aws.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class CaregiverService {

    private final CaregiverRepository caregiverRepository;
    private final ResumeRepository resumeRepository;
    private final S3Uploader s3Uploader;

    public Caregiver findCaregiverByLoginId(String loginId) {

        return caregiverRepository.findByLoginId(loginId).orElse(null);
    }

    public void registerResume(Caregiver caregiver, Resume resume) {
        caregiver.setResumeRegistered(true);
        resumeRepository.save(resume);
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
}
