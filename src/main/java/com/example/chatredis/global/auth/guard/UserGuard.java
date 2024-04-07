package com.example.chatredis.global.auth.guard;

import com.example.chatredis.domain.user.entity.UserRole;
import com.example.chatredis.domain.user.repository.UserRepository;
import com.example.chatredis.domain.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGuard extends Guard{

    private final UserRepository userRepository;
    private final List<UserRole> roleTypes = List.of(UserRole.ROLE_ADMIN);

    @Override
    protected List<UserRole> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return userRepository.findById(id)
                .filter(user -> user.getId().equals(AuthUtil.extractUserId()))
                .isPresent();
    }
}
