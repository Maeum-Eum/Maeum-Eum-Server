package com.five.Maeum_Eum.dto.user.caregiver.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CaregiverMypageDTO {

    private String name;
    private String address;
    private String ProfileImage;  // 프로필 사진 URL. 없으면 기본 URL을 지정.
    private boolean isResumeRegistered; // 이력서 등록 여부
    private boolean isJobOpen; // 구인 공개 여부
    private int savedEldersCount; // 저장한 어르신 수
    private int managerContactCount; // 연락 온 관리자 수
}
