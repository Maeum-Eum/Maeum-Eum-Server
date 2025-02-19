package com.five.Maeum_Eum.dto.user.caregiver.main.response;

/*센터 이름 , 생성시간 , 수정시간 , 협의여부 , 시급 , 제목 ,
해당 어르신의 요구사항과 같거나 높은 거 노출 , 센터 id , 어르신 이름 , [월/수/금] 오전 - 식사 배변 이동 일상 , 어르신 id */

import com.five.Maeum_Eum.entity.user.caregiver.Apply;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.Manager;

import java.time.LocalDateTime;
import java.util.List;

public record ApplyCaregiverDto(
        Long applyId,
        ApprovalStatus approvalStatus,
        Long centerId,
        String centerName,
        Long elderId,
        String elderName,

        boolean negotiable,
        String title ,
        List<String> satisfyTasks, //해당 어르신의 요구사항과 같거나 높은 거 노출
        int wage ,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {

      public static ApplyCaregiverDto from(Apply apply, Manager manager , Caregiver caregiver, String title , List<String> satisfyTasks){
          return new ApplyCaregiverDto(
                  apply.getApplyId(),
                  apply.getApprovalStatus(),
                  manager.getCenter().getCenterId(),
                  manager.getCenter().getCenterName(),
                  apply.getElder().getElderId(),
                  apply.getElder().getElderName(),
                  caregiver.getResume().getNegotiableTime(),
                  title,
                  satisfyTasks,
                  caregiver.getResume().getWage(),
                  apply.getCreatedAt(),
                  apply.getUpdatedAt()
          );
      }



}
