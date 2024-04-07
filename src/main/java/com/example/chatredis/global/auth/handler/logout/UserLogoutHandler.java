package com.example.chatredis.global.auth.handler.logout;

import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import com.example.chatredis.global.util.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class UserLogoutHandler implements LogoutHandler {

    private final RedisService redisService;
    private final JwtTokenizer jwtTokenizer;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String accessToken = jwtTokenizer.getHeaderAccessToken(request);
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
            Date expiration = jwtTokenizer.getClaims(accessToken, base64EncodedSecretKey).getBody().getExpiration();
            long now = new Date().getTime();
            redisService.setBlackList(accessToken, "accessToken", expiration.getTime() - now);
        } catch (ExpiredJwtException eje) {
            log.error("### 토큰이 만료되었습니다. 그대로 로그아웃합니다.");
        }

    }
}
