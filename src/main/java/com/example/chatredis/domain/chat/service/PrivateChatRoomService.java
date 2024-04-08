package com.example.chatredis.domain.chat.service;

import com.example.chatredis.domain.chat.dto.request.ChatRoomDeleteRequest;
import com.example.chatredis.domain.chat.dto.response.*;
import com.example.chatredis.domain.chat.entity.PrivateChatMessage;
import com.example.chatredis.domain.chat.entity.PrivateChatRoom;
import com.example.chatredis.domain.chat.repository.PrivateChatMessageJpaRepository;
import com.example.chatredis.domain.chat.repository.PrivateChatRoomJpaRepository;
import com.example.chatredis.domain.user.dto.response.UserDto;
import com.example.chatredis.domain.user.entity.User;
import com.example.chatredis.domain.user.repository.UserRepository;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
@Service
@Transactional
public class PrivateChatRoomService {

    private final UserRepository userRepository;
    private final PrivateChatRoomJpaRepository privateChatRoomJpaRepository;
    private final PrivateChatMessageJpaRepository privateChatMessageJpaRepository;

    @PreAuthorize("@userGuard.check(#userId)")
    @Transactional(readOnly = true)
    public ChatRoomListResponseDto getAllChatRoomByUserId(long userId) {

        User user = getUserById(userId);

        List<PrivateChatRoom> privateChatRoomList = privateChatRoomJpaRepository.findAllAccessibleByUser(user);

        List<SimpleChatRoomResponseDto> simpleChatRoomResponseDtoList = privateChatRoomList.stream()
                .map(this::mapToSimpleChatRoomResponseDto)
                .sorted(comparing(SimpleChatRoomResponseDto::getLastMessageTime).reversed())
                .collect(Collectors.toList());

        return new ChatRoomListResponseDto(simpleChatRoomResponseDtoList);
    }

    @PreAuthorize("@privateChatRoomGuard.check(#chatRoomId)")
    @Transactional(readOnly = true)
    public PrivateChatRoomDto getChatRoomByIdWithUsers(long userId, long chatRoomId) {

        User user = getUserById(userId);

        PrivateChatRoom privateChatRoom = getPrivateChatRoomByIdAndUser(chatRoomId, user);


        return PrivateChatRoomDto.builder()
                .chatRoomId(privateChatRoom.getId())
                .chatRoomName(privateChatRoom.getChatRoomName())
                .userDto(UserDto.toDto(privateChatRoom.getOtherUser(user)))
                .build();
    }


    @PreAuthorize("@privateChatRoomGuard.check(#chatRoomId)")
    public ChatRoomDeleteResponseDto exitChatroom(long chatRoomId, ChatRoomDeleteRequest req) {
        User user = getUserById(req.getUserId());

        PrivateChatRoom privateChatRoom = getPrivateChatRoomByIdAndUser(chatRoomId, user);

        PrivateChatMessage privateChatMessage = privateChatMessageJpaRepository.findTop1ByPrivateChatRoomOrderByCreatedAtDesc(privateChatRoom);

        privateChatRoom.exitChatRoom(user, privateChatMessage.getId());

        if (privateChatRoom.isDeletable()) {
            privateChatRoomJpaRepository.delete(privateChatRoom);
        } else {
            privateChatRoomJpaRepository.save(privateChatRoom);
        }

        return new ChatRoomDeleteResponseDto(privateChatRoom.getId());
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

    private SimpleChatRoomResponseDto mapToSimpleChatRoomResponseDto(PrivateChatRoom privateChatRoom) {
        PrivateChatMessage privateChatMessage = privateChatMessageJpaRepository.findTop1ByPrivateChatRoomOrderByCreatedAtDesc(privateChatRoom);
        return new SimpleChatRoomResponseDto(privateChatMessage.getContent(), privateChatMessage.getCreatedAt(), privateChatRoom.getChatRoomName(), privateChatRoom.getId());
    }

    private PrivateChatRoom getPrivateChatRoomByIdAndUser(long chatRoomId, User user) {
        return privateChatRoomJpaRepository.findByUserAndId(user, chatRoomId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));
    }
}