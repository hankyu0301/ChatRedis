package com.example.chatredis.domain.user.entity;

import com.example.chatredis.domain.user.dto.request.UserUpdateRequest;
import com.example.chatredis.domain.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    public User(String email, String password, String username, String nickname) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.userRole = UserRole.ROLE_NORMAL;
    }

    public User update(UserUpdateRequest req) {
        this.username = req.getUsername();
        this.nickname = req.getNickname();
        return this;
    }

    public void assignAdmin() {
        this.userRole = UserRole.ROLE_ADMIN;
    }

}
