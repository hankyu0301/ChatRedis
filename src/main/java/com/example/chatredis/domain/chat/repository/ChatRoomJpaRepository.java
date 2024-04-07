package com.example.chatredis.domain.chat.repository;

import com.example.chatredis.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findById(Long id);

    @Query("select cr from ChatRoom cr join fetch cr.chatRoomUserList where cr.id = :id")
    Optional<ChatRoom> findByIdWithChatRoomUser(Long id);

}
