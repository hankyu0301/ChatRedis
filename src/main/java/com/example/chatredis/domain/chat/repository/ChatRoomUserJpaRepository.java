package com.example.chatredis.domain.chat.repository;

import com.example.chatredis.domain.chat.entity.ChatRoom;
import com.example.chatredis.domain.chat.entity.ChatRoomUser;
import com.example.chatredis.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserJpaRepository extends JpaRepository<ChatRoomUser, Long> {

    Optional<ChatRoomUser> findByUserAndChatRoom(User user, ChatRoom chatRoom);

    @Query("SELECT cru FROM ChatRoomUser cru LEFT JOIN FETCH cru.deletedMessageIds WHERE cru.user = :user AND cru.chatRoom = :chatRoom")
    Optional<ChatRoomUser> findByUserAndChatRoomWithDeletedMessageIds(User user, ChatRoom chatRoom);

    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);

    // 회원 entity로 채팅방 찾기
    @Query("select cru from ChatRoomUser cru join fetch cru.chatRoom where cru.user = :user")
    List<ChatRoomUser> findAllWithChatRoomByUser(@Param("user") User user);

    List<ChatRoomUser> findChatRoomUserIdsByChatRoom(ChatRoom chatRoom);
}
