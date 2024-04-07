package com.example.chatredis.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 메시지 삭제 요청")
public class ChatMessageDeleteRequest {

    @NotNull(message = "회원 ID는 필수 입력 값입니다.")
    @Positive(message = "올바른 회원 ID를 입력해주세요.")
    @Schema(description = "회원 ID", example = "1")
    private Long userId;

    @NotNull(message = "채팅방 ID는 필수 입력 값입니다.")
    @Positive(message = "올바른 채팅방 ID를 입력해주세요.")
    @Schema(description = "채팅방 ID")
    private Long chatRoomId;


    @NotNull(message = "채팅 메시지 ID는 필수 입력 값입니다.")
    @Positive(message = "올바른 채팅 메시지 ID를 입력해주세요.")
    @Schema(description = "채팅 메시지 ID")
    private Long chatMessageId;

}
