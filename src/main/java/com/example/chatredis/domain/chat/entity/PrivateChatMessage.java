package com.example.chatredis.domain.chat.entity;


import com.example.chatredis.domain.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "private_chat_message")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String username;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private PrivateChatRoom privateChatRoom;

    public PrivateChatMessage(Long userId, String username, String content, PrivateChatRoom privateChatRoom) {
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.privateChatRoom = privateChatRoom;
    }
}
