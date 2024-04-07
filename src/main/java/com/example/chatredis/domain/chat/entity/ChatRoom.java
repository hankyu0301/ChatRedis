package com.example.chatredis.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@Table(name = "group_chat_room")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_name")
    private String chatRoomName;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> messageList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomUser> chatRoomUserList = new ArrayList<>();

    public ChatRoom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }
}
