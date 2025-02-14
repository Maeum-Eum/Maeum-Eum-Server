package com.five.Maeum_Eum.service.user.caregiver;

import com.five.Maeum_Eum.repository.caregiver.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ResumeService {

    private final ResumeRepository resumeRepository;

    // 이력서의 경우, 임시 저장할 수 있다.



    // 프로필 사진 이미지 업로
}
