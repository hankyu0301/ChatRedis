package com.example.chatredis.global.auth.handler.login;

import com.example.chatredis.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        log.info("### Authenticated successfully!");
        log.info("### 로그인 정보 이메일: " + user.getEmail());
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("userId", user.getId());

        // JSON 변환 및 응답 전송
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(responseData));
        } catch (IOException e) {
            log.error("Error occurred while sending response", e);
        }
    }
}
