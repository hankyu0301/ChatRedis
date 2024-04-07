package com.example.chatredis.global.auth.utils;

import com.example.chatredis.domain.user.entity.User;
import com.example.chatredis.domain.user.repository.UserRepository;
import com.example.chatredis.global.auth.jwt.DelegateTokenUtil;
import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class AccessTokenRenewalUtil {
    private final UserRepository userRepository;
    private final DelegateTokenUtil delegateTokenUtil;
    private final JwtTokenizer jwtTokenizer;

    public Token renewAccessToken(HttpServletRequest request) {
        try {
            String refreshToken = jwtTokenizer.getHeaderRefreshToken(request);
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
            String email = jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey).getBody().getSubject();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
            String newAccessToken = delegateTokenUtil.delegateAccessToken(user);
            return Token.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
        } catch (CustomException | ExpiredJwtException e) {
            throw e;
        }
    }

}