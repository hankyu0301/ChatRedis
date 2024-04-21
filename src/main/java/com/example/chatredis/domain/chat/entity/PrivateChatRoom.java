package com.example.chatredis.domain.chat.entity;

import com.example.chatredis.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@Table(name = "private_chat_room")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_name")
    private String chatRoomName;

    @Column(name = "deleted_by_from_user")
    private boolean deletedByFromUser = false;

    @Column(name = "deleted_by_to_user")
    private boolean deletedByToUser = false;

    @Column(name = "from_last_message_id")
    private Long fromUserLastMessageId = 0L;

    @Column(name = "to_last_message_id")
    private Long toUserLastMessageId = 0L;

    @ElementCollection
    @CollectionTable(name = "private_chat_fromUser_deleted_message_ids", joinColumns = @JoinColumn(name = "private_chat_room_id"))
    private List<Long> fromUserDeletedMessageIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "private_chat_toUser_deleted_message_ids", joinColumns = @JoinColumn(name = "private_chat_room_id"))
    private List<Long> toUserDeletedMessageIds = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @OneToMany(mappedBy = "privateChatRoom", orphanRemoval = true)
    private List<PrivateChatMessage> privateChatMessageList = new ArrayList<>();

    private void deletedByFromUser() {
        this.deletedByFromUser = true;
    }

    private void deletedByToUser() {
        this.deletedByToUser = true;
    }

    // 사용자가 fromUser인 경우 deletedByFromUser 호출, 그렇지 않은 경우 deletedByToUser 호출
    public boolean isDeletable() {
        return isDeletedByFromUser() && isDeletedByToUser();
    }

    public void clearDeletedFlags() {
        this.deletedByFromUser = false;
        this.deletedByToUser = false;
    }

    public void exitChatRoom(User user, Long messageId) {
        // 해당 사용자가 fromUser인 경우 deletedByFromUser 호출
        if (user.equals(fromUser)) {
            deletedByFromUser();
            updateFromUserLastMessageId(messageId);
        }
        // 그렇지 않은 경우 deletedByToUser 호출
        else  {
            deletedByToUser();
            updateToUserLastMessageId(messageId);
        }
    }

    private void updateFromUserLastMessageId(Long messageId) {
        this.fromUserLastMessageId = messageId;
    }

    private void updateToUserLastMessageId(Long messageId) {
        this.toUserLastMessageId = messageId;
    }

    public Long getLastMessageId(User user) {
        return user.equals(fromUser) ? fromUserLastMessageId : toUserLastMessageId;
    }

    public void deleteMessage(Long deletedMessageId, User user) {
        if (user.equals(fromUser)) {
            this.fromUserDeletedMessageIds.add(deletedMessageId);
        } else this.toUserDeletedMessageIds.add(deletedMessageId);

    }

    @Builder
    public PrivateChatRoom(String chatRoomName, User fromUser, User toUser) {
        this.chatRoomName = chatRoomName;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.deletedByFromUser = this.deletedByToUser = false;
        this.fromUserLastMessageId = this.toUserLastMessageId = 0L;
        this.fromUserDeletedMessageIds = toUserDeletedMessageIds = new ArrayList<>();
    }

    public List<Long> getDeletedMessageIds(User user) {
        if (user.equals(fromUser)) {
            return fromUserDeletedMessageIds;
        } else return toUserDeletedMessageIds;
    }

    public boolean isParticipant(Long userId) {
        return fromUser.getId().equals(userId) || toUser.getId().equals(userId);
    }

    public User getOtherUser(User currentUser) {
        if (currentUser.equals(fromUser)) {
            return toUser;
        } else if (currentUser.equals(toUser)) {
            return fromUser;
        } else {
            throw new IllegalArgumentException("User is not a participant of this chat room");
        }
    }
}
