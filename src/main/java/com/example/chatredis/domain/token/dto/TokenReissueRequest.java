package com.example.chatredis.domain.token.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenReissueRequest {
    private String accessToken;
    private String refreshToken;
}
