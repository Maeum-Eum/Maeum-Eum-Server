package com.five.Maeum_Eum.dto.user.manager.request;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkReqDto {

     Long elderId;
     Long caregiverId;

     public BookmarkReqDto(Long elderId , Long caregiverId){
         this.elderId = elderId;
         this.caregiverId = caregiverId;
     }

     public static ManagerBookmark toEntity(Manager manager , Elder elder , Caregiver caregiver){
         return ManagerBookmark.builder()
                 .caregiver(caregiver)
                 .elder(elder)
                 .manager(manager)
                 .build();
     }

}
