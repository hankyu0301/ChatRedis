package com.example.chatredis.domain.chat.dto.request;

import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "개인 메시지 발신 요청")
public class PrivateChatMessageCreateRequest {

    @Null
    @Schema(description = "회원 id", example = "1", hidden = true)
    private Long userId;

    @NotNull(message = "메시지를 수신할 회원 id는 필수 입력 값입니다.")
    @Positive(message = "메시지를 수신할 회원 id를 입력해주세요.")
    @Schema(description = "회원 id", example = "2")
    private Long toUser;

    @NotBlank(message = "메시지 내용은 필수 입력 값입니다.")
    @Schema(description = "메시지 내용", example = "안녕하세요.")
    private String content;

    public void setUserIdFromJwt(String jwt, JwtTokenizer jwtTokenizer) {
        Map<String, Object> claims = jwtTokenizer.verifyJws(jwt.replace("Bearer ", ""));
        Long userId = Long.parseLong((String) claims.get("userId"));
        setUserId(userId);
    }
}
