package com.example.chatredis.global.event.chat;

import com.example.chatredis.domain.chat.dto.response.PrivateChatMessageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateChatMessageCreatedEvent {

    private Long receiverId;
    private Long senderId;
    private String senderName;
    private Long chatRoomId;
    private String chatRoomName;
    private String content;

    public PrivateChatMessageCreatedEvent(Long receiverId, String chatRoomName, PrivateChatMessageDto dto) {
        this.receiverId = receiverId;
        this.senderId = dto.getUserId();
        this.senderName = dto.getUsername();
        this.chatRoomId = dto.getChatRoomId();
        this.chatRoomName = chatRoomName;
        this.content = dto.getContent();
    }
}
