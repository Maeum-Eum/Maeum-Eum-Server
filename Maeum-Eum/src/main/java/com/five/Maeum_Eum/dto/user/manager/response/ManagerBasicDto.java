package com.five.Maeum_Eum.dto.user.manager.response;

import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.manager.Manager;

// 관리자 마이페이지의 응답 DTO
public record ManagerBasicDto(
        Long managerId,
        String name,
        String phoneNumber,
        Long centerId,
        int sentContacts, // 관리자가 연락한 요양보호사 수
        int bookmarks // 관리자가 한 요양보호사 북마크 개수
) {
    public static ManagerBasicDto from(Manager manager , Center center , int sentContacts , int bookmarks){
        return new ManagerBasicDto(
                manager.getManagerId(),
                manager.getName(),
                manager.getPhoneNumber(),
                center.getCenterId(),
                sentContacts,
                bookmarks
        );

    }
}
