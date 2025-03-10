package com.five.Maeum_Eum.dto.user.caregiver.main.response;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;

import java.time.LocalDateTime;
import java.util.List;

/*이력서 생성날짜 , 이력서 제목 , 시급 , 협의가능 , 요양보호사가 가능한 업무 최대 7개 ,이력서 id , 저장했는지 여부*/
public record RecommendedCaregiverDto(
     Long caregiverId,
     String caregiverName,
     Long resumeId,
     String title,
     boolean negotiable,
     List<String> possibleTasks,

     int wage,

     boolean isBookmarks,

     Long bookmarkId,

     LocalDateTime createdAt


) {
    public static RecommendedCaregiverDto from(Caregiver caregiver , String title , List<String> possibleTasks , Boolean isMarked , ManagerBookmark managerBookmark) {
        return new RecommendedCaregiverDto(
                caregiver.getCaregiverId(),
                caregiver.getName(),
                caregiver.getResume().getResumeId(),
                title,
                caregiver.getResume().getNegotiableTime(),
                possibleTasks,
                caregiver.getResume().getWage(),
                isMarked,
                managerBookmark.getBookmarkId(),
                caregiver.getResume().getCreatedAt()
        );
    }

    public static RecommendedCaregiverDto of(Caregiver caregiver, String title, List<String> possibleTasks, Boolean isMarked) {
        return new RecommendedCaregiverDto(
                caregiver.getCaregiverId(),
                caregiver.getName(),
                caregiver.getResume().getResumeId(),
                title,
                caregiver.getResume().getNegotiableTime(),
                possibleTasks,
                caregiver.getResume().getWage(),
                isMarked,
                null,
                caregiver.getResume().getCreatedAt()
        );
    }
}
