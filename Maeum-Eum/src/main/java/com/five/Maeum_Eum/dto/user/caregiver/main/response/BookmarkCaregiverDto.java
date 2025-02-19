package com.five.Maeum_Eum.dto.user.caregiver.main.response;

import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;

import java.time.LocalDateTime;
import java.util.List;

/*요양보호사 주소 , 요양보호사 이름 , caregiversupport ,이력서 id , 수정시간 , 생성 시간 , 요양보호사 주소*/
public record BookmarkCaregiverDto(
       Long bookmarkId,
       Long caregiverId,
       String caregiverName,
       List<String> caregiverSupport,
       Long resumeId,
       String address,
       LocalDateTime createdAt,
       LocalDateTime updatedAt
) {
    public static BookmarkCaregiverDto from(ManagerBookmark managerBookmark , List<String> caregiverSupport){
        return new BookmarkCaregiverDto(
                managerBookmark.getBookmarkId(),
                managerBookmark.getCaregiver().getCaregiverId(),
                managerBookmark.getCaregiver().getName(),
                caregiverSupport,
                managerBookmark.getCaregiver().getResume().getResumeId(),
                managerBookmark.getCaregiver().getAddress(),
                managerBookmark.getCreatedAt(),
                managerBookmark.getUpdatedAt()
        );
    }


}
