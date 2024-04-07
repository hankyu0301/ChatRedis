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
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅방 삭제 요청")
public class ChatRoomDeleteRequest {

    @NotNull(message = "회원 id는 필수 입력 값입니다.")
    @Positive(message = "올바른 회원 id를 입력해주세요.")
    @Schema(description = "회원 id", example = "1")
    private Long userId;
}
