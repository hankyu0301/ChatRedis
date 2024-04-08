package com.example.chatredis.domain.chat.controller;

import com.example.chatredis.domain.chat.dto.request.ChatMessageDeleteRequest;
import com.example.chatredis.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.chatredis.domain.chat.dto.request.PrivateChatMessageCreateRequest;
import com.example.chatredis.domain.chat.dto.response.ChatMessageDeleteResponseDto;
import com.example.chatredis.domain.chat.dto.response.PrivateChatMessageReadResponseDto;
import com.example.chatredis.domain.chat.service.PrivateChatMessageService;
import com.example.chatredis.domain.util.BaseResponse;
import com.example.chatredis.domain.util.FailResponse;
import com.example.chatredis.domain.util.SuccessResponse;
import com.example.chatredis.global.aop.AssignUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "개인 채팅 메시지",
        description = "개인 채팅 메시지를 위한 API"
)
@Slf4j
@RestController
@RequiredArgsConstructor
public class PrivateChatMessageController {

    private final PrivateChatMessageService privateChatMessageService;

    /**
     * websocket "/pub/private/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/private/chat/message")
    @AssignUserId
    public void message(@Header("Authorization")String jwt, PrivateChatMessageCreateRequest req){
        privateChatMessageService.sendMessage(req);
    }


    /**
     * 특정 채팅방의 최근 메시지 20개를 가져옴
     * */
    @Operation(
            summary = "채팅방 메시지 조회",
            description = "채팅방의 최근 메시지를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 최근 메시지 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PrivateChatMessageReadResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            - 채팅방이 존재하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/api/v1/private/chat/message")
    public ResponseEntity<BaseResponse<PrivateChatMessageReadResponseDto>> messageList(@Valid @ModelAttribute ChatMessageReadCondition cond){
        PrivateChatMessageReadResponseDto result = privateChatMessageService.findLatestMessage(cond);

        SuccessResponse<PrivateChatMessageReadResponseDto> response = SuccessResponse.<PrivateChatMessageReadResponseDto>builder()
                .data(result)
                .message("채팅 메시지 목록 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "채팅 메시지 삭제",
            description = "채팅 메시지 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅 메시지 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatMessageDeleteRequest.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            - 메시지가 존재하지 않습니다.
                            - 채팅방이 존재하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PatchMapping("/api/v1/private/chat/message")
    public ResponseEntity<BaseResponse<ChatMessageDeleteResponseDto>> deleteMessage(@Valid @RequestBody ChatMessageDeleteRequest req){
        ChatMessageDeleteResponseDto result = privateChatMessageService.deleteChatMessage(req);

        SuccessResponse<ChatMessageDeleteResponseDto> response = SuccessResponse.<ChatMessageDeleteResponseDto>builder()
                .data(result)
                .message("채팅 메시지 삭제 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}
