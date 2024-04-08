package com.example.chatredis.domain.chat.repository;

import com.example.chatredis.domain.chat.entity.PrivateChatRoom;
import com.example.chatredis.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrivateChatRoomJpaRepository extends JpaRepository<PrivateChatRoom, Long> {

    @Query("select pcr from PrivateChatRoom pcr where (pcr.fromUser = :fromUser and pcr.toUser = :toUser) or (pcr.fromUser = :toUser and pcr.toUser = :fromUser)")
    Optional<PrivateChatRoom> findByUserAndUser(User fromUser, User toUser);

    @Query("SELECT pcr FROM PrivateChatRoom pcr WHERE (pcr.fromUser = :user AND pcr.deletedByFromUser = false) OR (pcr.toUser = :user AND pcr.deletedByToUser = false)")
    List<PrivateChatRoom> findAllAccessibleByUser(User user);

    @Query("select pcr from PrivateChatRoom pcr join fetch pcr.fromUser join fetch pcr.toUser where ((pcr.fromUser = :user or pcr.toUser = :user) and pcr.id = :chatRoomId)")
    Optional<PrivateChatRoom> findByUserAndId(User user, Long chatRoomId);

    @Query("select pcr " +
            "   from PrivateChatRoom pcr " +
            "   where (pcr.fromUser = :user or pcr.toUser = :user) " +
            "       and pcr.id = :chatRoomId")
    Optional<PrivateChatRoom> findByUserAndIdWithDeleteMessageIds(User user, Long chatRoomId);

    @Query("select pcr from PrivateChatRoom pcr join fetch pcr.fromUser join fetch pcr.toUser where pcr.id = :chatRoomId")
    Optional<PrivateChatRoom> findByIdWithUsers(long chatRoomId);
}
