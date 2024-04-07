package com.example.chatredis.domain.chat.repository;

import com.example.chatredis.domain.chat.entity.ChatMessage;
import com.example.chatredis.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long>, CustomChatMessageRepository {

    ChatMessage findTop1ByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

    @Query("SELECT cm FROM ChatMessage cm join fetch cm.chatRoom WHERE cm.id = :id")
    Optional<ChatMessage> findByIdWithChatRoom(Long id);

}
