package com.example.chatredis.domain.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "간단한 채팅방 정보 응답")
public class SimpleChatRoomResponseDto {

    @Schema(description = "마지막 메시지", example = "안녕하세요.")
    private String lastMessage;

    @Schema(description = "마지막 메시지 발신 시간")
    private LocalDateTime lastMessageTime;

    @Schema(description = "채팅방 이름", example = "채팅방1")
    private String chatRoomName;

    @Schema(description = "채팅방 id", example = "1")
    private Long chatRoomId;
}