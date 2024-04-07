package com.example.chatredis.domain.chat.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@RedisHash(value = "inviteCode", timeToLive = 60 * 5)
public class ChatRoomInviteCode {

    @Id
    private Long id;

    private Long userId;

    private Long chatRoomId;

    @Indexed
    private String inviteCode;

    public ChatRoomInviteCode(Long chatRoomId, Long userId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.inviteCode = UUID.randomUUID().toString();
    }
}
