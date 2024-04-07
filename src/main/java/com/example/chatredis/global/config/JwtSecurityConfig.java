package com.example.chatredis.global.config;

import com.example.chatredis.global.auth.filter.JwtAuthenticationFilter;
import com.example.chatredis.global.auth.filter.JwtVerificationFilter;
import com.example.chatredis.global.auth.handler.login.UserAuthenticationFailureHandler;
import com.example.chatredis.global.auth.handler.login.UserAuthenticationSuccessHandler;
import com.example.chatredis.global.auth.jwt.DelegateTokenUtil;
import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import com.example.chatredis.global.auth.utils.AccessTokenRenewalUtil;
import com.example.chatredis.global.util.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@RequiredArgsConstructor
public class JwtSecurityConfig extends AbstractHttpConfigurer<JwtSecurityConfig, HttpSecurity> {
    private final JwtTokenizer jwtTokenizer;
    private final DelegateTokenUtil delegateTokenUtil;
    private final AccessTokenRenewalUtil accessTokenRenewalUtil;
    private final RedisService redisService;

    @Override
    public void configure(HttpSecurity builder) {
        AuthenticationManager authenticationManager = builder
                .getSharedObject(AuthenticationManager.class);

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                authenticationManager,
                delegateTokenUtil,
                jwtTokenizer);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/users/login");
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(new UserAuthenticationSuccessHandler());
        jwtAuthenticationFilter.setAuthenticationFailureHandler(new UserAuthenticationFailureHandler());

        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, accessTokenRenewalUtil,
                redisService);

        builder
                .addFilter(jwtAuthenticationFilter)
                .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
    }
}