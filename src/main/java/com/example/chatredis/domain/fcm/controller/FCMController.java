package com.example.chatredis.domain.fcm.controller;

import com.example.chatredis.domain.fcm.dto.response.FCMTokenDto;
import com.example.chatredis.domain.fcm.service.FCMService;
import com.example.chatredis.domain.util.BaseResponse;
import com.example.chatredis.domain.util.FailResponse;
import com.example.chatredis.domain.util.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(
        name = "FCM 토큰",
        description = "FCM 토큰을 위한 API"
)
public class FCMController {

    private final FCMService fcmService;

    @Operation(
            summary = "FCM 토큰 발급",
            description = "FCM 토큰을 발급 받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "FCM 토큰 발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FCMTokenDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/api/v1/fcm/{userId}")
    public ResponseEntity<BaseResponse<FCMTokenDto>> saveOrUpdate(@Parameter(description = "사용자 ID")
                                                           @PathVariable Long userId){
        FCMTokenDto result = fcmService.saveOrUpdateToken(userId);

        SuccessResponse<FCMTokenDto> response = SuccessResponse.<FCMTokenDto>builder()
                .data(result)
                .message("FCM 토큰 발급 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "FCM 토큰 삭제",
            description = "FCM 토큰을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "FCM 토큰 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FCMTokenDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            - FCM 토큰이 존재하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @DeleteMapping("/api/v1/fcm/{userId}")
    public ResponseEntity<BaseResponse<FCMTokenDto>> delete(@Parameter(description = "사용자 ID")
                                                       @PathVariable Long userId){
        FCMTokenDto result = fcmService.deleteToken(userId);

        SuccessResponse<FCMTokenDto> response = SuccessResponse.<FCMTokenDto>builder()
                .data(result)
                .message("FCM 토큰 삭제 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}
