package com.example.chatredis.domain.fcm.entity;

import com.example.chatredis.domain.fcm.dto.response.FCMTokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@RedisHash(value = "FCMToken", timeToLive = 60 * 60 * 24 * 60)
public class FCMToken {

    @Id
    private String id;

    private String AccessToken;

    @Indexed
    private Long userId;

    public FCMTokenDto of() {
        return new FCMTokenDto(this.id, this.AccessToken, this.userId);
    }
}
