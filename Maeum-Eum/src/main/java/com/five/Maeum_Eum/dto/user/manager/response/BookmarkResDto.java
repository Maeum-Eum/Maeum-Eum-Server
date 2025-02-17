package com.five.Maeum_Eum.dto.user.manager.response;


import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;

public record BookmarkResDto(

        Long elderId,
        String elderName,

        Long ManagerId,
        Long caregiverId

) {

    public static BookmarkResDto from(ManagerBookmark managerBookmark){
        return new BookmarkResDto(
                managerBookmark.getElder().getElderId(),
                managerBookmark.getElder().getElderName(),
                managerBookmark.getManager().getManagerId(),
                managerBookmark.getCaregiver().getCaregiverId()
        );
    }
}
