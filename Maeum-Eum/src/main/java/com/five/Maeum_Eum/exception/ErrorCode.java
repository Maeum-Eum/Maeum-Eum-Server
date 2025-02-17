package com.five.Maeum_Eum.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"이미 존재하는 회원입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),



    //Manager
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "관리자가 아닌 사용자입니다."),
    CONTACT_NOT_FOUND(HttpStatus.NOT_FOUND, "매칭 요청이 존재하지 않습니다"),


    CENTER_NOT_FOUND(HttpStatus.NOT_FOUND, "센터가 존재하지 않습니다."),
    INVALID_ROLE(HttpStatus.UNAUTHORIZED, "해당 경로에 대한 권한이 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력이 존재합니다."),
    RESUME_NOT_REGISTERED(HttpStatus.FORBIDDEN, "이력서가 존재하지 않는 유저입니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다.");

    private final HttpStatus status;
    private final String message;
}
