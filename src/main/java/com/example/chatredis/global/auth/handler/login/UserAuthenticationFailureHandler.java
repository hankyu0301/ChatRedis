package com.example.chatredis.global.auth.handler.login;

import com.example.chatredis.global.auth.error.AuthenticationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        log.error("### Authentication failed: {}", exception.getMessage());
        log.error("### Authentication failed: {}", exception.getClass().getName());
        AuthenticationError.setErrorResponse(HttpStatus.BAD_REQUEST, 400, "로그인에 실패하였습니다.", response);
    }


}
