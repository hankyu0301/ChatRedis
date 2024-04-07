package com.example.chatredis.global.auth.handler.login;

import com.example.chatredis.global.auth.error.AuthenticationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        log.error("### UserAuthenticationEntryPoint Error!! : " + authException.getMessage());
        AuthenticationError.setErrorResponse(HttpStatus.UNAUTHORIZED, 401, "인증에 실패했습니다.", response);
    }
}
