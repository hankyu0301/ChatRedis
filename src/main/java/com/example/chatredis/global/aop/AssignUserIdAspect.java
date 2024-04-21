package com.example.chatredis.global.aop;

import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AssignUserIdAspect {

    private final JwtTokenizer jwtTokenizer;

    @Around("@annotation(com.example.chatredis.global.aop.AssignUserId) && args(jwt, req)")
    public void assignUserId(ProceedingJoinPoint joinPoint, String jwt, Object req) throws Throwable {
        Long userId = extractUserIdFromJwt(jwt);

        invokeSetUserIdMethod(req, userId);

        joinPoint.proceed();
    }

    private Long extractUserIdFromJwt(String jwt) {
        Map<String, Object> claims = jwtTokenizer.verifyJws(jwt.replace("Bearer ", ""));
        return Long.parseLong((String) claims.get("userId"));
    }

    private void invokeSetUserIdMethod(Object req, Long userId) {
        Class<?> reqClass = req.getClass();
        Optional<Method> setUserIdMethod = getSetUserIdMethod(reqClass);

        if (setUserIdMethod.isPresent()) {
            try {
                setUserIdMethod.get().invoke(req, userId);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to invoke setUserId method", e);
            }
        } else {
            throw new IllegalArgumentException("setUserId method not found in request object");
        }
    }

    private Optional<Method> getSetUserIdMethod(Class<?> clazz) {
        try {
            return Optional.of(clazz.getMethod("setUserId", Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }
}