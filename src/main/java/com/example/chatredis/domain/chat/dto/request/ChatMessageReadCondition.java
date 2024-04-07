package com.example.chatredis.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅방 최근 메시지 조회 요청")
public class ChatMessageReadCondition {

    @NotNull(message = "회원 id는 필수 입력 값입니다.")
    @Schema(description = "회원 id", example = "1")
    private Long userId;

    @NotNull(message = "채팅방 id는 필수 입력 값입니다.")
    @Positive(message = "올바른 채팅방 id를 입력해주세요.")
    @Schema(description = "채팅방 id", example = "1")
    private Long chatRoomId;

    @Schema(description = "마지막 채팅 메시지 id", example = "1000")
    private Long lastChatMessageId = Long.MAX_VALUE;

    @NotNull(message = "페이지 크기를 입력해주세요.")
    @Positive(message = "올바른 페이지 크기를 입력해주세요.")
    @Schema(description = "페이지 크기", example = "20")
    private Integer size;
}