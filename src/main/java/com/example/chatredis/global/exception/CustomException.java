package com.example.chatredis.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;

    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        int status = exceptionCode.getHttpStatusCode();
        this.status = HttpStatus.valueOf(status);
    }

    public static CustomException of(ExceptionCode exceptionCode) {
        return new CustomException(exceptionCode);
    }
}