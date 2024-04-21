package com.example.chatredis.global.config;

import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import com.example.chatredis.global.util.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

/**
 * Websocket 을 통하여 요청이 들어오면 Intercept 하여 JWT 인증 구현 및 사전처리
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenizer jwtTokenizer;
    private final RedisService redisService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            String jws = accessToken.replace("Bearer ", "");
            try {
                if (redisService.hasKeyBlackList(jws))
                    throw new CustomException(ExceptionCode.ALREADY_LOGGED_OUT_TOKEN_EXCEPTION);

                jwtTokenizer.verifyJws(jws);
                /*  액세스 토큰이 만료된 경우 진입*/
            } catch (CustomException ce){
                log.error("### 이미 로그아웃된 토큰입니다.");
                throw new MessageHandlingException(message, "이미 로그아웃된 토큰입니다.", ce);
            }catch (ExpiredJwtException eje) {
                    log.error("### 토큰이 만료됐습니다.");
                throw new MessageHandlingException(message, "토큰이 만료됐습니다.", eje);
            } catch (MalformedJwtException mje) {
                log.error("### 올바르지 않은 토큰 형식입니다.");
                throw new MessageHandlingException(message, "올바르지 않은 토큰 형식입니다.", mje);
            } catch (SignatureException se) {
                log.error("### 토큰의 서명이 잘못 됐습니다. 변조 데이터일 가능성이 있습니다.");
                throw new MessageHandlingException(message, "토큰의 서명이 잘못 됐습니다. 변조 데이터일 가능성이 있습니다.", se);
            }
        }
            return message;
    }
}
