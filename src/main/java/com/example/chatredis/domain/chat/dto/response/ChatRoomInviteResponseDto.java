package com.example.chatredis.domain.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅방 초대 응답")
public class ChatRoomInviteResponseDto {

    @Schema(description = "초대한 회원 id", example = "1")
    private Long inviterId;

    @Schema(description = "초대 받은 회원 id", example = "1")
    private Long targetId;

    @Schema(description = "채팅방 id", example = "1")
    private Long chatRoomId;
}
