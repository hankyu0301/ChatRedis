package com.example.chatredis.domain.fcm.service;

import com.example.chatredis.domain.fcm.dto.request.FCMMessageDto;
import com.example.chatredis.domain.fcm.dto.response.FCMTokenDto;
import com.example.chatredis.domain.fcm.entity.FCMToken;
import com.example.chatredis.domain.fcm.repository.FCMTokenRepository;
import com.example.chatredis.domain.user.repository.UserRepository;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static com.example.chatredis.global.exception.ExceptionCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FCMService {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;
    @Value("${firebase.app.url}")
    private String appUrl;
    private final String PREFIX_ACCESS_TOKEN = "Bearer ";
    private final Boolean DEFAULT_VALIDATE_ONLY = false;
    private final FCMTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    /*  로그인 시에 호출되는 토큰발급, 갱신 메소드*/
    public FCMTokenDto saveOrUpdateToken(Long userId) {
        // 이미 저장된 FCMToken을 삭제하고 새로 저장
        if(!userRepository.existsById(userId)) {
            throw new CustomException(NOT_FOUND_USER);
        }

        fcmTokenRepository.findByUserId(userId).ifPresent(fcmTokenRepository::delete);

        FCMToken fcmToken = fcmTokenRepository.save(
                FCMToken.builder()
                        .AccessToken(getAccessToken())
                        .userId(userId)
                        .build()
        );

        return fcmToken.of();
    }

    /*  로그아웃 시에 호출되는 토큰 삭제 메소드*/
    public FCMTokenDto deleteToken(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new CustomException(NOT_FOUND_USER);
        }

        FCMToken fcmToken = fcmTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_FCM_TOKEN));

        fcmTokenRepository.delete(fcmToken);

        return fcmToken.of();
    }

    public void sendMessage(FCMMessageDto.Data data) {

        //알림 요청 받는 사람의 FCM Token이 존재하는지 확인
        FCMToken fcmToken = fcmTokenRepository.findByUserId(data.getReceiverId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_FCM_TOKEN));

        //메시지 만들기
        String message = makeMessage(fcmToken.getAccessToken(), data);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, PREFIX_ACCESS_TOKEN + getAccessToken());

        HttpEntity<String> httpEntity = new HttpEntity<>(message, httpHeaders);

        ResponseEntity<String> exchange = restTemplate.exchange(
                appUrl,
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        if (exchange.getStatusCode().isError()) {
            log.error("firebase 접속 에러 = {}", exchange.getBody());
        }
    }

    private String makeMessage(String targetToken, FCMMessageDto.Data data) {

        FCMMessageDto.Message message = new FCMMessageDto.Message(targetToken, data);

        FCMMessageDto fcmMessage = new FCMMessageDto(DEFAULT_VALIDATE_ONLY, message);

        try {
            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            throw new CustomException(ExceptionCode.JSON_PARSING_ERROR);
        }
    }

    private String getAccessToken() {

        try {
            final GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();

            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new CustomException(GOOGLE_REQUEST_TOKEN_ERROR);
        }
    }
}
