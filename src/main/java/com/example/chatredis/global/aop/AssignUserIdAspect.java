package com.example.chatredis.global.aop;

import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AssignUserIdAspect {

    private final JwtTokenizer jwtTokenizer;

    @Around("@annotation(org.springframework.messaging.handler.annotation.MessageMapping) && args(jwt, req)")
    public Object assignUserIdFromJwt(ProceedingJoinPoint joinPoint, String jwt, Object req) throws Throwable {
        // 요청 객체의 setUserIdFromJwt 메서드를 호출하여 사용자 ID 설정
        Method setUserIdFromJwtMethod = req.getClass().getMethod("setUserIdFromJwt", String.class, JwtTokenizer.class);
        setUserIdFromJwtMethod.invoke(req, jwt, jwtTokenizer);

        // 원래 메서드 실행
        return joinPoint.proceed(new Object[]{jwt, req});
    }

}