package com.example.chatredis;

import com.example.chatredis.domain.chat.dto.request.ChatRoomCreateRequest;
import com.example.chatredis.domain.chat.entity.ChatRoom;
import com.example.chatredis.domain.chat.repository.ChatRoomJpaRepository;
import com.example.chatredis.domain.chat.service.ChatRoomService;
import com.example.chatredis.domain.user.entity.User;
import com.example.chatredis.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class StubData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
       /* for (long i = 1; i <= 10; i++) {
            userRepository.save(newUserData(i));
        }*/
    }

    private User newUserData(long i) {
        return userRepository.save(new User("finebears@naver.com" + i, passwordEncoder.encode("123456a!"), "finebears" + i, "finebears" + i));
    }
}
