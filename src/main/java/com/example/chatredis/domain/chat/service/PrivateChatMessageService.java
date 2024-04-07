package com.example.chatredis.domain.chat.service;

import com.example.chatredis.domain.chat.dto.request.ChatMessageDeleteRequest;
import com.example.chatredis.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.chatredis.domain.chat.dto.request.PrivateChatMessageCreateRequest;
import com.example.chatredis.domain.chat.dto.response.ChatMessageDeleteResponseDto;
import com.example.chatredis.domain.chat.dto.response.PrivateChatMessageDto;
import com.example.chatredis.domain.chat.dto.response.PrivateChatMessageReadResponseDto;
import com.example.chatredis.domain.chat.entity.PrivateChatMessage;
import com.example.chatredis.domain.chat.entity.PrivateChatRoom;
import com.example.chatredis.domain.chat.repository.PrivateChatMessageJpaRepository;
import com.example.chatredis.domain.chat.repository.PrivateChatRoomJpaRepository;
import com.example.chatredis.domain.user.entity.User;
import com.example.chatredis.domain.user.repository.UserRepository;
import com.example.chatredis.global.event.chat.PrivateChatMessageCreatedEvent;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class PrivateChatMessageService {

    private final PrivateChatMessageJpaRepository privateChatMessageJpaRepository;
    private final PrivateChatRoomJpaRepository privateChatRoomJpaRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,Object> chatRedisTemplate;
    private final ChannelTopic privateChannelTopic;
    private final ApplicationEventPublisher publisher;

    public void sendMessage(PrivateChatMessageCreateRequest req) {
        User fromUser = getUser(req.getUserId());
        User toUser = getUser(req.getToUser());
        PrivateChatRoom privateChatRoom = findOrCreatePrivateChatRoom(fromUser, toUser);
        PrivateChatMessageDto privateChatMessageDto = createPrivateChatMessageDto(privateChatRoom, fromUser, req.getContent());
        PrivateChatMessage privateChatMessage = savePrivateChatMessageToDatabase(privateChatMessageDto.toEntity(privateChatRoom));
        privateChatMessageDto.setChatMessageId(privateChatMessage.getId());
        publishMessage(privateChatMessageDto);
        publisher.publishEvent(new PrivateChatMessageCreatedEvent(toUser.getId(), privateChatRoom.getChatRoomName(), privateChatMessageDto));
    }

    @PreAuthorize("@privateChatRoomGuard.check(#cond.chatRoomId)")
    @Transactional(readOnly = true)
    public PrivateChatMessageReadResponseDto findLatestMessage(ChatMessageReadCondition cond) {
        User user = getUser(cond.getUserId());

        PrivateChatRoom privateChatRoom = getPrivateChatRoom(user, cond.getChatRoomId());

        List<Long> deletedMessageIds = privateChatRoom.getDeletedMessageIds(user);

        return PrivateChatMessageReadResponseDto.toDto(privateChatMessageJpaRepository.findLatestMessages(cond, deletedMessageIds, privateChatRoom.getLastMessageId(user)));
    }

    @PreAuthorize("@privateChatRoomGuard.check(#req.chatRoomId)")
    public ChatMessageDeleteResponseDto deleteChatMessage(ChatMessageDeleteRequest req) {
        User user = getUser(req.getUserId());
        PrivateChatMessage privateChatMessage = getPrivateChatMessage(req.getChatMessageId());
        PrivateChatRoom privateChatRoom = getPrivateChatRoom(user, req.getChatRoomId());
        privateChatRoom.deleteMessage(privateChatMessage.getId(), user);
        savePrivateChatRoomToDatabase(privateChatRoom);
        return new ChatMessageDeleteResponseDto(privateChatMessage.getId());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

    private PrivateChatRoom findOrCreatePrivateChatRoom(User fromUser, User toUser) {
        return privateChatRoomJpaRepository.findByUserAndUser(fromUser, toUser)
                .map(room -> {
                    room.clearDeletedFlags();
                    return room;
                })
                .orElseGet(() -> createPrivateChatRoom(fromUser, toUser));
    }

    private PrivateChatRoom createPrivateChatRoom(User fromUser, User toUser) {
        return PrivateChatRoom.builder()
                .chatRoomName(fromUser.getUsername() + " , " + toUser.getUsername())
                .fromUser(fromUser)
                .toUser(toUser)
                .build();
    }

    private PrivateChatMessageDto createPrivateChatMessageDto(PrivateChatRoom privateChatRoom, User fromUser, String content) {
        return PrivateChatMessageDto.builder()
                .chatRoomId(privateChatRoom.getId())
                .userId(fromUser.getId())
                .username(fromUser.getUsername())
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void publishMessage(PrivateChatMessageDto privateChatMessageDto) {
        try {
            chatRedisTemplate.convertAndSend(privateChannelTopic.getTopic(), objectMapper.writeValueAsString(privateChatMessageDto));
        } catch (JsonProcessingException e) {
            throw new CustomException(ExceptionCode.JSON_PARSING_ERROR);
        }
    }

    private PrivateChatRoom getPrivateChatRoom(User user, Long chatRoomId) {
        return privateChatRoomJpaRepository.findByUserAndIdWithDeleteMessageIds(user, chatRoomId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));
    }

    private PrivateChatMessage getPrivateChatMessage(Long chatMessageId) {
        return privateChatMessageJpaRepository.findById(chatMessageId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_MESSAGE));
    }

    private PrivateChatMessage savePrivateChatMessageToDatabase(PrivateChatMessage privateChatMessage) {
        return privateChatMessageJpaRepository.save(privateChatMessage);
    }

    private void savePrivateChatRoomToDatabase(PrivateChatRoom privateChatRoom) {
        privateChatRoomJpaRepository.save(privateChatRoom);
    }
}
