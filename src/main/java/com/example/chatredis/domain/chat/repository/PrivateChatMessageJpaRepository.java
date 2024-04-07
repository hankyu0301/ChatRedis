package com.example.chatredis.domain.chat.repository;

import com.example.chatredis.domain.chat.entity.PrivateChatMessage;
import com.example.chatredis.domain.chat.entity.PrivateChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateChatMessageJpaRepository extends JpaRepository<PrivateChatMessage, Long>, CustomPrivateChatMessageRepository {

    PrivateChatMessage findTop1ByPrivateChatRoomOrderByCreatedAtDesc(PrivateChatRoom pcr);

}
