package com.five.Maeum_Eum.dto.user.manager.response;

import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.manager.Manager;

// 관리자 마이페이지의 응답 DTO
public record ManagerBasicDto(
        Long managerId,
        String name,
        String phoneNumber,
        Long centerId,
        String centerName,

        boolean hasCar,
        String oneLineIntro,
        int sentContacts, // 관리자가 연락한 요양보호사 수
        int bookmarks ,// 관리자가 한 요양보호사 북마크 개수

        int applys // 관리자가 받은 지원서 수
) {
    public static ManagerBasicDto from(Manager manager , Center center , int sentContacts , int bookmarks , int applys){
        return new ManagerBasicDto(
                manager.getManagerId(),
                manager.getName(),
                manager.getPhoneNumber(),
                center.getCenterId(),
                center.getCenterName(),
                center.isHasCar(),
                center.getOneLineIntro(),
                sentContacts,
                bookmarks,
                applys
        );
    }

    public static ManagerBasicDto of(Manager manager , Center center){
        return  new ManagerBasicDto(
                manager.getManagerId(),
                manager.getName(),
                manager.getPhoneNumber(),
                center.getCenterId(),
                center.getCenterName(),
                center.isHasCar(),
                center.getOneLineIntro(),
                0,
                0,
                0
        );
    }
}
