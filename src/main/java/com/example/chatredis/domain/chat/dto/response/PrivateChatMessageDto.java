package com.example.chatredis.domain.chat.dto.response;

import com.example.chatredis.domain.chat.entity.PrivateChatMessage;
import com.example.chatredis.domain.chat.entity.PrivateChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "채팅 메시지 응답")
public class PrivateChatMessageDto {

    @Schema(description = "채팅 메시지 ID", example = "1")
    private Long chatMessageId;

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "회원 ID", example = "1")
    private Long userId;

    @Schema(description = "회원 이름", example = "John")
    private String username;

    @Schema(description = "메시지 내용", example = "안녕하세요.")
    private String content;

    @Schema(description = "메시지 작성 시간")
    private LocalDateTime createdAt;

    public PrivateChatMessage toEntity(PrivateChatRoom privateChatRoom) {
        return new PrivateChatMessage(
                this.userId,
                this.username,
                this.content,
                privateChatRoom);
    }

    @Builder
    public PrivateChatMessageDto(Long chatMessageId, Long chatRoomId, Long userId, String username, String content, LocalDateTime createdAt) {
        this.chatMessageId = chatMessageId;
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }
}
