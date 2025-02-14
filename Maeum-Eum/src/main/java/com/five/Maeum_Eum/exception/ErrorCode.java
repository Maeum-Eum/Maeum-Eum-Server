package com.five.Maeum_Eum.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    CENTER_NOT_FOUND(HttpStatus.NOT_FOUND, "센터가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
