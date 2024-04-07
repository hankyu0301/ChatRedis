package com.example.chatredis.domain.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "API 실패 응답")
public class FailResponse<T> {

    @Schema(description = "성공 여부", example = "false")
    private boolean success;

    @Schema(description = "응답 데이터")
    private T data;

    @Schema(description = "응답 메시지", example = "상황에 맞는 에러 메시지")
    private String message;

    public FailResponse(T data, String message) {
        this.success = false;
        this.data = data;
        this.message = message;
    }

    public static <T> FailResponse<T> of(T data, String message) {
        return new FailResponse<>(data, message);
    }
}