package com.example.chatredis.domain.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Schema(description = "API 응답")
public abstract class BaseResponse<T> {
    @Schema(description = "성공 여부", example = "true")
    private boolean success;
    @Schema(description = "응답 데이터")
    private T data;

    @Schema(description = "응답 메시지", example = "성공 응답 메시지")
    private String message;
}
