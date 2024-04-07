package com.example.chatredis.global.auth.guard;

import com.example.chatredis.domain.chat.entity.ChatRoom;
import com.example.chatredis.domain.chat.repository.ChatRoomJpaRepository;
import com.example.chatredis.domain.user.entity.UserRole;
import com.example.chatredis.domain.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomGuard extends Guard {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final List<UserRole> roleTypes = List.of(UserRole.ROLE_ADMIN);

    @Override
    protected List<UserRole> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long chatRoomId) {
        return  chatRoomJpaRepository.findByIdWithChatRoomUser(chatRoomId)
                .map(ChatRoom::getChatRoomUserList)
                .filter(chatRoomUsers -> chatRoomUsers.stream()
                        .anyMatch(
                                chatRoomUser ->
                                        chatRoomUser.getUser().getId().equals(AuthUtil.extractUserId()))
                )
                .isPresent();
    }
}
