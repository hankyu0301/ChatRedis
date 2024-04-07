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
@Schema(description = "채팅방 삭제 응답")
public class ChatRoomDeleteResponseDto {

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;
}
