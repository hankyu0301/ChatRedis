package com.example.chatredis.domain.fcm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FCMMessageDto {
    private boolean    validateOnly;
    private Message    message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private String token;
        private Data data;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data{
        private Long receiverId;
        private Long senderId;
        private String senderName;
        private Long chatRoomId;
        private String chatRoomName;
        private String content;
    }

}