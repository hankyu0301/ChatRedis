package com.example.chatredis.domain.chat.entity;

import com.example.chatredis.domain.chat.dto.response.ChatMessageDto;
import com.example.chatredis.domain.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "group_chat_message")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String username;

    @Column(name = "content")
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    public ChatMessage(Long userId, String username, String content, MessageType type, ChatRoom chatRoom) {
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.type = type;
        this.chatRoom = chatRoom;
    }

    public ChatMessageDto of() {
        return new ChatMessageDto(
                this.id,
                this.chatRoom.getId(),
                this.chatRoom.getChatRoomName(),
                this.userId,
                this.username,
                this.content,
                this.getType(),
                this.getCreatedAt()
        );
    }

    public enum MessageType {
        ENTER, TALK, QUIT
    }
}
