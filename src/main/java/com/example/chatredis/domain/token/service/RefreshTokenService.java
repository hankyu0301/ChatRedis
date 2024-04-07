package com.example.chatredis.domain.token.service;

import com.example.chatredis.domain.token.entity.RefreshToken;
import com.example.chatredis.domain.token.repository.RefreshTokenRepository;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveTokenInfo(Long userId, String refreshToken, String accessToken) {
        refreshTokenRepository.save(new RefreshToken(String.valueOf(userId), refreshToken, accessToken));
    }

    public void removeRefreshToken(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken)
            .ifPresent(refreshTokenRepository::delete);
    }

    @Transactional(readOnly = true)
    public RefreshToken getTokenByAccessToken(String accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken)
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN));
    }

}
