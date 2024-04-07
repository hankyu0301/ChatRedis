package com.example.chatredis.domain.token.controller;

import com.example.chatredis.domain.token.dto.TokenResponse;
import com.example.chatredis.domain.util.BaseResponse;
import com.example.chatredis.domain.util.FailResponse;
import com.example.chatredis.domain.util.SuccessResponse;
import com.example.chatredis.global.auth.jwt.JwtTokenizer;
import com.example.chatredis.global.auth.utils.AccessTokenRenewalUtil;
import com.example.chatredis.global.auth.utils.Token;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "토큰", description = "토큰 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final JwtTokenizer jwtTokenizer;
    private final AccessTokenRenewalUtil accessTokenRenewalUtil;

    @Operation(
            summary = "토큰 갱신",
            description = "토큰을 갱신합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "토큰 갱신 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description =
                                    """
                                    - 리프레시 토큰을 찾을 수 없음
                                    """,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FailResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description =
                                    """
                                    - 회원을 찾을 수 없습니다.
                                    """,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FailResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/api/v1/tokens")
    public ResponseEntity<BaseResponse<TokenResponse>> getToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            Token token = accessTokenRenewalUtil.renewAccessToken(request);
            jwtTokenizer.setHeaderAccessToken(response, token.getAccessToken());
            jwtTokenizer.setHeaderRefreshToken(response, token.getRefreshToken());

            TokenResponse result = new TokenResponse(token.getAccessToken(), token.getRefreshToken());

            SuccessResponse<TokenResponse> tokenResponseSuccessResponse = SuccessResponse.<TokenResponse>builder()
                    .data(result)
                    .message("채팅 메시지 목록 조회 성공")
                    .build();

            return ResponseEntity.ok(tokenResponseSuccessResponse);
        } catch (ExpiredJwtException je) {
            log.error("### 리프레쉬 토큰을 찾을 수 없음");
            jwtTokenizer.resetHeaderRefreshToken(response);
            throw new CustomException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN);
        } catch (CustomException ce) {
            log.error("### 해당 회원을 찾을 수 없음");
            jwtTokenizer.resetHeaderRefreshToken(response);
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
    }
}
