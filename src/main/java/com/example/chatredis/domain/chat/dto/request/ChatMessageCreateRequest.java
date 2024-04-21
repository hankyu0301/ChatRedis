package com.example.chatredis.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "메시지 발신 요청")
public class ChatMessageCreateRequest {

    @NotNull(message = "채팅방 id는 필수 입력 값입니다.")
    @Positive(message = "올바른 채팅방 id를 입력해주세요.")
    @Schema(description = "채팅방 id", example = "1")
    private Long chatRoomId;

    @Null(message = "유저 id는 필수 입력 값입니다.")
    @Schema(description = "유저 id", example = "1", hidden = true)
    private Long userId;

    @NotBlank(message = "메시지 내용은 필수 입력 값입니다.")
    @Schema(description = "메시지 내용", example = "안녕하세요.")
    private String content;

}
