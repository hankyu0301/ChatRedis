package com.example.chatredis.domain.fcm.controller;

import com.example.chatredis.domain.fcm.dto.request.FCMTokenCreateRequest;
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
import jakarta.validation.Valid;
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
            summary = "FCM 토큰 저장",
            description = "FCM 토큰을 저장합니다.."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "토큰 저장 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = FCMTokenCreateRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "FCM 토큰 저장 성공",
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
    @PostMapping("/api/v1/fcm")
    public ResponseEntity<BaseResponse<FCMTokenDto>> saveOrUpdate(@Parameter(description = "사용자 ID")
                                                           @Valid @RequestBody FCMTokenCreateRequest fcmTokenCreateRequest){
        FCMTokenDto result = fcmService.saveOrUpdateToken(fcmTokenCreateRequest);

        SuccessResponse<FCMTokenDto> response = SuccessResponse.<FCMTokenDto>builder()
                .data(result)
                .message("FCM 토큰 저장 성공")
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
