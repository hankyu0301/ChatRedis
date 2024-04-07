package com.example.chatredis.global.event.chat;

import com.example.chatredis.domain.chat.dto.response.ChatMessageDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatMessageCreatedEvent {

    private List<Long> receiverIds = new ArrayList<>();
    private Long senderId;
    private String senderName;
    private Long chatRoomId;
    private String chatRoomName;
    private String content;

    public ChatMessageCreatedEvent(List<Long> receiverIds, ChatMessageDto chatMessageDto) {
        this.receiverIds.addAll(receiverIds);
        this.senderId = chatMessageDto.getUserId();
        this.senderName = chatMessageDto.getUsername();
        this.chatRoomId = chatMessageDto.getChatRoomId();
        this.chatRoomName = chatMessageDto.getChatRoomName();
        this.content = chatMessageDto.getContent();
    }
}
