package com.example.chatredis.domain.chat.controller;

import com.example.chatredis.domain.chat.dto.request.ChatRoomDeleteRequest;
import com.example.chatredis.domain.chat.dto.response.*;
import com.example.chatredis.domain.chat.service.PrivateChatRoomService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "개인 채팅방",
        description = "개인 채팅방을 위한 API"
)
@Slf4j
@RestController
@RequiredArgsConstructor
public class PrivateChatRoomController {

    private final PrivateChatRoomService privateChatRoomService;


    @Operation(
            summary = "채팅방 목록 조회",
            description = "채팅방 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomListResponseDto.class)
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
    @GetMapping("/api/v1/private/chatRoom/list/users/{userId}")
    public ResponseEntity<BaseResponse<ChatRoomListResponseDto>> getAllChatRoomByUserId(@Parameter(description = "사용자 ID", example = "1")
                                                                                        @PathVariable long userId) {

        ChatRoomListResponseDto result = privateChatRoomService.getAllChatRoomByUserId(userId);

        SuccessResponse<ChatRoomListResponseDto> response =  SuccessResponse.<ChatRoomListResponseDto>builder()
                .data(result)
                .message("채팅방 목록 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "채팅방 정보 조회",
            description = "채팅방 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomDto.class)
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
    @GetMapping("/api/v1/private/chatRoom/{chatRoomId}/users/{userId}")
    public ResponseEntity<BaseResponse<ChatRoomDto>> getChatRoomByIdWithUsers(@Parameter(description = "사용자 ID", example = "1")
                                                                            @PathVariable long userId,
                                                                            @Parameter(description = "사용자 ID", example = "1")
                                                                            @PathVariable long chatRoomId) {

        ChatRoomDto result = privateChatRoomService.getChatRoomByIdWithUsers(userId, chatRoomId);

        SuccessResponse<ChatRoomDto> response =  SuccessResponse.<ChatRoomDto>builder()
                .data(result)
                .message("채팅방 정보 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "채팅방 퇴장",
            description = "채팅방에서 퇴장합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "채팅방 퇴장 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ChatRoomDeleteRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 퇴장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomDeleteResponseDto.class)
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
    @PatchMapping("/api/v1/private/chatRoom/{chatRoomId}/exit")
    public ResponseEntity<BaseResponse<ChatRoomDeleteResponseDto>> exitChatRoom(@Parameter(description = "채팅방 ID", example = "1")
                                                                                @PathVariable long chatRoomId,
                                                                                @Valid @RequestBody ChatRoomDeleteRequest req) {

        ChatRoomDeleteResponseDto result = privateChatRoomService.exitChatroom(chatRoomId, req);

        SuccessResponse<ChatRoomDeleteResponseDto> response =  SuccessResponse.<ChatRoomDeleteResponseDto>builder()
                .data(result)
                .message("채팅방 퇴장 성공")
                .build();

        return ResponseEntity.ok(response);

    }
}


