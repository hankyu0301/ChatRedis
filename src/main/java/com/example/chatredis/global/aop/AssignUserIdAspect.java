package com.example.chatredis.global.aop;

import com.example.chatredis.domain.chat.dto.request.ChatMessageCreateRequest;
import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AssignUserIdAspect {

    private final JwtTokenizer jwtTokenizer;

    @Around("@annotation(org.springframework.messaging.handler.annotation.MessageMapping) && args(jwt, req, ..)")
    public Object assignUserIdFromJwt(ProceedingJoinPoint joinPoint, String jwt, ChatMessageCreateRequest req) throws Throwable {
        // JWT에서 사용자 ID 추출
        Map<String, Object> claims = jwtTokenizer.verifyJws(jwt.replace("Bearer ", ""));
        log.info("claims: {}", claims);
        Long userId = Long.parseLong((String) claims.get("userId"));

        // 사용자 ID 설정 후 메서드 실행
        req.setUserId(userId);
        return joinPoint.proceed(new Object[]{jwt, req});
    }

}