package com.example.chatredis.domain.chat.service;

import com.example.chatredis.domain.chat.dto.response.ChatMessageDto;
import com.example.chatredis.domain.chat.dto.response.PrivateChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber 가 해당 메시지를 받아 처리한다.
     */
    public void sendGroupMessage(String publishMessage) {
        try {
            log.info("Redis에서 발행된 메시지를 받아옵니다.");
            log.info("발행된 메시지 = " + publishMessage);

            // ChatMessageDto 객채로 맵핑
            ChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, ChatMessageDto.class);

            // Websocket 구독자에게 채팅 메시지 Send
            log.info("Websocket 구독자에게 채팅 메시지를 Send 합니다.");
            log.info("chatMessageDto = " + chatMessageDto.toString());
            messagingTemplate.convertAndSend("/sub/chat/group/chatRoom/" + chatMessageDto.getChatRoomId(), chatMessageDto);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendPrivateMessage(String publishMessage) {
        try {
            // ChatMessageDto 객채로 맵핑
            PrivateChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, PrivateChatMessageDto.class);
            System.out.println("chatMessageDto = " + chatMessageDto);
            // Websocket 구독자에게 채팅 메시지 Send
            messagingTemplate.convertAndSend("/sub/chat/private/chatRoom/" + chatMessageDto.getChatRoomId(), chatMessageDto);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
