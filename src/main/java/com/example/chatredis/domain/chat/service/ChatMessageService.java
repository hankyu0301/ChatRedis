package com.example.chatredis.domain.chat.service;

import com.example.chatredis.domain.chat.dto.request.ChatMessageCreateRequest;
import com.example.chatredis.domain.chat.dto.request.ChatMessageDeleteRequest;
import com.example.chatredis.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.chatredis.domain.chat.dto.response.ChatMessageDeleteResponseDto;
import com.example.chatredis.domain.chat.dto.response.ChatMessageDto;
import com.example.chatredis.domain.chat.dto.response.ChatMessageReadResponseDto;
import com.example.chatredis.domain.chat.entity.ChatMessage;
import com.example.chatredis.domain.chat.entity.ChatRoom;
import com.example.chatredis.domain.chat.entity.ChatRoomUser;
import com.example.chatredis.domain.chat.repository.ChatMessageJpaRepository;
import com.example.chatredis.domain.chat.repository.ChatRoomJpaRepository;
import com.example.chatredis.domain.chat.repository.ChatRoomUserJpaRepository;
import com.example.chatredis.domain.user.entity.User;
import com.example.chatredis.domain.user.repository.UserRepository;
import com.example.chatredis.global.aop.AssignUserId;
import com.example.chatredis.global.event.chat.ChatMessageCreatedEvent;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatMessageService {

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomUserJpaRepository chatRoomUserJpaRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String,Object> chatRedisTemplate;
    private final ChannelTopic groupChannelTopic;
    private final ApplicationEventPublisher publisher;

    public void saveChatMessage(ChatMessageCreateRequest req) {
        User user = getUserById(req.getUserId());
        ChatRoom chatRoom = getChatRoomById(req.getChatRoomId());
        validateUserInChatRoom(user, chatRoom);

        List<ChatRoomUser> chatRoomUsers = chatRoomUserJpaRepository.findChatRoomUserIdsByChatRoom(chatRoom);
        List<Long> chatRoomUserIds =
                chatRoomUsers.stream()
                        .map(ChatRoomUser::getId)
                        .collect(Collectors.toList());

        ChatMessageDto chatMessageDto = createChatMessage(user, req.getContent(), chatRoom);
        ChatMessage chatMessage = chatMessageDto.toEntity(chatRoom);
        saveChatMessageToDatabase(chatMessage);
        chatMessageDto.setChatMessageId(chatMessage.getId());
        publishChatMessage(chatMessageDto);
        publisher.publishEvent(new ChatMessageCreatedEvent(chatRoomUserIds, chatMessageDto));
    }

    @PreAuthorize("@chatRoomGuard.check(#cond.chatRoomId)")
    @Transactional(readOnly = true)
    public ChatMessageReadResponseDto findLatestMessage(ChatMessageReadCondition cond) {
        User user = getUserById(cond.getUserId());
        ChatRoom chatRoom = getChatRoomById(cond.getChatRoomId());
        ChatRoomUser chatRoomUser = getChatRoomUser(user, chatRoom);
        return createChatMessageReadResponseDto(chatRoomUser.getDeletedMessageIds(), cond);
    }

    @PreAuthorize("@chatRoomGuard.check(#req.chatRoomId)")
    public ChatMessageDeleteResponseDto deleteChatMessage(ChatMessageDeleteRequest req) {
        User user = getUserById(req.getUserId());
        ChatMessage chatMessage = getChatMessageById(req.getChatMessageId());
        ChatRoomUser chatRoomUser = getChatRoomUser(user, chatMessage.getChatRoom());
        deleteAndSaveChatMessage(chatMessage, chatRoomUser);
        return new ChatMessageDeleteResponseDto(chatMessage.getId());
    }

    private void deleteAndSaveChatMessage(ChatMessage chatMessage, ChatRoomUser chatRoomUser) {
        chatRoomUser.getDeletedMessageIds().add(chatMessage.getId());
        chatRoomUserJpaRepository.save(chatRoomUser);
    }

    private ChatMessage getChatMessageById(Long chatMessageId) {
        return chatMessageJpaRepository.findByIdWithChatRoom(chatMessageId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_MESSAGE));
    }

    private ChatRoomUser getChatRoomUser(User user, ChatRoom chatRoom) {
        return chatRoomUserJpaRepository.findByUserAndChatRoomWithDeletedMessageIds(user, chatRoom)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

    private ChatRoom getChatRoomById(long chatRoomId) {
        return chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));
    }

    private void validateUserInChatRoom(User user, ChatRoom chatRoom) {
        if (!chatRoomUserJpaRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER);
        }
    }

    private ChatMessageDto createChatMessage(User user, String content, ChatRoom chatRoom) {
        return ChatMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getChatRoomName())
                .username(user.getUsername())
                .userId(user.getId())
                .content(content)
                .type(ChatMessage.MessageType.TALK)
                .createdAt(LocalDateTime.now())
                .build();
    }
    private void saveChatMessageToDatabase(ChatMessage chatMessage) {
        chatMessageJpaRepository.save(chatMessage);
    }

    private void publishChatMessage(ChatMessageDto chatMessageDto) {
        chatRedisTemplate.convertAndSend(groupChannelTopic.getTopic(), chatMessageDto);
    }

    private ChatMessageReadResponseDto createChatMessageReadResponseDto(List<Long> deletedMessageIds, ChatMessageReadCondition cond) {
        return ChatMessageReadResponseDto.toDto(chatMessageJpaRepository.findLatestMessages(deletedMessageIds, cond));
    }
}