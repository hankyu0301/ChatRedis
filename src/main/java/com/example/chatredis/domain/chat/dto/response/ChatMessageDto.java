package com.example.chatredis.domain.chat.dto.response;

import com.example.chatredis.domain.chat.entity.ChatMessage;
import com.example.chatredis.domain.chat.entity.ChatRoom;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Schema(description = "채팅 메시지 응답")
public class ChatMessageDto {

    @Schema(description = "채팅 메시지 ID", example = "1")
    private Long chatMessageId;

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "채팅방 이름", example = "채팅방1")
    private String chatRoomName;

    @Schema(description = "회원 ID", example = "1")
    private Long userId;

    @Schema(description = "회원 이름", example = "John")
    private String username;

    @Schema(description = "메시지 내용", example = "안녕하세요.")
    private String content;

    @Schema(description = "메시지 타입 (ENTER, QUIT, TALK)", example = "ENTER")
    @JsonProperty("type")
    private String type;

    @Schema(description = "메시지 작성 시간")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    public ChatMessage toEntity(ChatRoom chatRoom) {
        return new ChatMessage(
                this.userId,
                this.username,
                this.content,
                ChatMessage.MessageType.valueOf(this.type),
                chatRoom);
    }

    @Builder
    public ChatMessageDto(Long chatMessageId, Long chatRoomId, String chatRoomName, Long userId, String username, String content, ChatMessage.MessageType type, LocalDateTime createdAt) {
        this.chatMessageId = chatMessageId;
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.type = type.name();
        this.createdAt = createdAt;
    }

}
