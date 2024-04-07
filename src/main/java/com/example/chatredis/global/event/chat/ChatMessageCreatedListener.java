package com.example.chatredis.global.event.chat;

import com.example.chatredis.domain.fcm.dto.request.FCMMessageDto;
import com.example.chatredis.domain.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageCreatedListener {

    private final FCMService fcmService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleAlarm(ChatMessageCreatedEvent event) {

        for(Long receiverId : event.getReceiverIds()) {
            FCMMessageDto.Data data = FCMMessageDto.Data.builder()
                    .receiverId(receiverId)
                    .senderId(event.getSenderId())
                    .senderName(event.getSenderName())
                    .chatRoomId(event.getChatRoomId())
                    .chatRoomName(event.getChatRoomName())
                    .content(event.getContent())
                    .build();
            fcmService.sendMessage(data);
        }

    }


}
