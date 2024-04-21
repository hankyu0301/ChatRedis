package com.example.chatredis.domain.util;

import com.example.chatredis.domain.user.entity.UserRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class AuthUtil {

    public static boolean isAuthenticated() {
        return getAuthentication() != null &&
                getAuthentication().isAuthenticated();
    }

    public static Long extractUserId() {
        return Long.valueOf((String) getAuthentication().getPrincipal());
    }

    public static Set<UserRole> extractUserRole() {
        return getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}