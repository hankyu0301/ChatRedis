package com.example.chatredis.domain.chat.controller;

import com.example.chatredis.domain.chat.dto.request.*;
import com.example.chatredis.domain.chat.dto.response.*;
import com.example.chatredis.domain.chat.service.ChatRoomService;
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
        name = "그룹 채팅방",
        description = "그룹 채팅방을 위한 API"
)
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

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
    @GetMapping("/api/v1/group/chatRoom/list/users/{userId}")
    public ResponseEntity<BaseResponse<ChatRoomListResponseDto>> getAllChatRoomByUserId(@Parameter(description = "사용자 ID", example = "1")
                                                                             @PathVariable long userId) {

        ChatRoomListResponseDto result = chatRoomService.getAllChatRoomByUserId(userId);

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
                            - 채팅방에 초대되지 않은 회원입니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/api/v1/group/chatRoom/{chatRoomId}/users/{userId}")
    public ResponseEntity<BaseResponse<ChatRoomDto>> getChatRoom(@Parameter(description = "채팅방 ID", example = "1")
                                                                 @PathVariable long chatRoomId,
                                                                 @Parameter(description = "회원 ID", example = "1")
                                                                 @PathVariable long userId) {
        ChatRoomDto result = chatRoomService.getChatRoomWithUserListByChatRoomId(userId, chatRoomId);

        SuccessResponse<ChatRoomDto> response =  SuccessResponse.<ChatRoomDto>builder()
                .data(result)
                .message("채팅방 정보 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "채팅방 생성",
            description = "채팅방을 생성합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "채팅방 생성 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ChatRoomCreateRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomCreateResponseDto.class)
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
    @PostMapping("/api/v1/group/chatRoom")
    public ResponseEntity<BaseResponse<ChatRoomCreateResponseDto>> createChatRoom(@Valid @RequestBody ChatRoomCreateRequest req) {

        ChatRoomCreateResponseDto result = chatRoomService.createChatRoom(req);

        SuccessResponse<ChatRoomCreateResponseDto> response =  SuccessResponse.<ChatRoomCreateResponseDto>builder()
                .data(result)
                .message("채팅방 생성 성공")
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
                            - 채팅방에 초대되지 않은 회원입니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PatchMapping("/api/v1/group/chatRoom/{chatRoomId}/exit")
    public ResponseEntity<BaseResponse<ChatRoomDeleteResponseDto>> exitChatRoom(@Parameter(description = "채팅방 ID", example = "1")
                                                                                    @PathVariable long chatRoomId,
                                                                                    @Valid @RequestBody ChatRoomDeleteRequest req) {

        ChatRoomDeleteResponseDto result = chatRoomService.exitChatroom(chatRoomId, req);

        SuccessResponse<ChatRoomDeleteResponseDto> response =  SuccessResponse.<ChatRoomDeleteResponseDto>builder()
                .data(result)
                .message("채팅방 퇴장 성공")
                .build();

        return ResponseEntity.ok(response);

    }

    @Operation(
            summary = "채팅방 초대",
            description = "채팅방에 회원을 초대합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "채팅방 초대 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ChatRoomInviteRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 초대 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomInviteResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            - 채팅방이 존재하지 않습니다.
                            - 채팅방에 초대되지 않은 회원입니다.
                            - 이미 초대된 회원입니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/api/v1/group/chatRoom/invite")
    public ResponseEntity<BaseResponse<ChatRoomInviteResponseDto>> inviteChatRoom(@Valid @RequestBody ChatRoomInviteRequest req) {

        ChatRoomInviteResponseDto result = chatRoomService.inviteChatRoom(req);

        SuccessResponse<ChatRoomInviteResponseDto> response =  SuccessResponse.<ChatRoomInviteResponseDto>builder()
                .data(result)
                .message("채팅방 초대 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "채팅방 초대 코드 생성",
            description = "채팅방 초대 코드를 생성 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "채팅방 초대 코드 생성 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ChatRoomInviteCodeCreateRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 초대 코드 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomInviteCodeResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            - 채팅방이 존재하지 않습니다.
                            - 채팅방에 초대되지 않은 회원입니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/api/v1/group/chatRoom/inviteCode")
    public ResponseEntity<BaseResponse<ChatRoomInviteCodeResponseDto>> createChatRoomInviteCode(@Valid @RequestBody ChatRoomInviteCodeCreateRequest req) {

        ChatRoomInviteCodeResponseDto result = chatRoomService.createInviteCode(req);

        SuccessResponse<ChatRoomInviteCodeResponseDto> response =  SuccessResponse.<ChatRoomInviteCodeResponseDto>builder()
                .data(result)
                .message("채팅방 초대 코드 생성 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "채팅방 입장",
            description = "채팅방에 입장 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "채팅방 입장 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ChatRoomJoinRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 입장 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomJoinResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            - 채팅방이 존재하지 않습니다.
                            - 초대 코드가 유효하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/api/v1/group/chatRoom/join")
    public ResponseEntity<BaseResponse<ChatRoomJoinResponseDto>> joinChatRoom(@Valid @RequestBody ChatRoomJoinRequest req) {

        ChatRoomJoinResponseDto result = chatRoomService.joinChatRoomByInviteCode(req);

        SuccessResponse<ChatRoomJoinResponseDto> response =  SuccessResponse.<ChatRoomJoinResponseDto>builder()
                .data(result)
                .message("채팅방 입장 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}
