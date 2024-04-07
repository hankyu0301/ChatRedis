package com.example.chatredis.domain.token.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
}

