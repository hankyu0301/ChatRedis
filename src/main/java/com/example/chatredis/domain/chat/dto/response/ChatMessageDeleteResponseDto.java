package com.example.chatredis.domain.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 메시지 삭제 응답")
public class ChatMessageDeleteResponseDto {

    @Schema(description = "삭제된 메시지 id", example = "1")
    private Long messageId;

}
