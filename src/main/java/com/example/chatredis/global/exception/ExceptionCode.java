package com.example.chatredis.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    NOT_FOUND_USER("회원을 찾을 수 없습니다.", 404),
    NOT_FOUND_REFRESH_TOKEN("리프레시 토큰을 찾을 수 없습니다.", 404),
    NOT_EXIST_CHAT_ROOM("채팅방이 존재하지 않습니다.", 404),
    NOT_EXIST_CHAT_MESSAGE("채팅 메시지가 존재하지 않습니다.", 404),
    NOT_FOUND_FCM_TOKEN("FCM 토큰을 찾을 수 없습니다.", 404),
    DUPLICATE_NICKNAME("이미 사용중인 닉네임입니다.", 400),
    DUPLICATE_EMAIL("이미 사용중인 이메일입니다.", 400),
    NOT_EXIST_CHAT_ROOM_USER("채팅방에 속한 유저가 아닙니다.", 400),
    INVALID_CHAT_ROOM_INVITE_CODE("초대 코드가 유효하지 않습니다.", 400),
    ALREADY_EXIST_CHAT_ROOM_USER("이미 채팅방에 속한 유저입니다.", 400),
    GOOGLE_REQUEST_TOKEN_ERROR("구글 요청 토큰 에러", 400),
    JSON_PARSING_ERROR("JSON 파싱 에러", 400),
    ALREADY_LOGGED_OUT_TOKEN_EXCEPTION("이미 로그아웃된 액세스 토큰입니다.", 400),
    UNAUTHORIZED_STRING("인증되지 않은 요청입니다.", 401);

    private final String message;
    private final int httpStatusCode;
}