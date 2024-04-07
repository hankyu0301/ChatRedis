package com.example.chatredis.domain.chat.entity;

import com.example.chatredis.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "group_chat_room_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_message_id")
    private Long firstMessageId = 0L;

    @ElementCollection
    @CollectionTable(name = "deleted_message_ids", joinColumns = @JoinColumn(name = "chatroom_user_id"))
    private List<Long> deletedMessageIds = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    //private boolean isAdmin;

    public ChatRoomUser(User user, ChatRoom chatRoom) {
        this.firstMessageId = 0L;
        this.deletedMessageIds = new ArrayList<>();
        this.user = user;
        this.chatRoom = chatRoom;
    }

    public void updateFirstMessageId(Long id) {
        this.firstMessageId = id;
    }
}
