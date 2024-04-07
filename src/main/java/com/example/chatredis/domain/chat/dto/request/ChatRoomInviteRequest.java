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
@Schema(description = "채팅방 초대 요청")
public class ChatRoomInviteRequest {

    @NotNull(message = "회원 id는 필수 입력 값입니다.")
    @Positive(message = "초대하는 회원 id를 입력해주세요.")
    @Schema(description = "초대하는 회원 id", example = "1")
    private Long inviterId;

    @NotNull(message = "회원 id는 필수 입력 값입니다.")
    @Positive(message = "초대할 회원 id를 입력해주세요.")
    @Schema(description = "초대할 회원 id", example = "2")
    private Long targetId;

    @NotNull(message = "채팅방 id는 필수 입력 값입니다.")
    @Positive(message = "올바른 채팅방 id를 입력해주세요.")
    @Schema(description = "채팅방 id", example = "1")
    private Long chatRoomId;
}
