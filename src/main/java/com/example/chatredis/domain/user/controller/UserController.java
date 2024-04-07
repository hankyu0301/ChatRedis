package com.example.chatredis.domain.user.controller;

import com.example.chatredis.domain.user.dto.request.UserCreateRequest;
import com.example.chatredis.domain.user.dto.response.UserCreateResponseDto;
import com.example.chatredis.domain.user.dto.response.UserDeleteResponseDto;
import com.example.chatredis.domain.user.dto.response.UserDto;
import com.example.chatredis.domain.user.dto.request.UserUpdateRequest;
import com.example.chatredis.domain.user.service.UserService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "회원",
        description = "회원을 위한 API"
)
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원 생성",
            description = "회원을 생성합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원 생성 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = UserCreateRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 이미 사용중인 닉네임입니다.
                            - 이미 사용중인 이메일입니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/api/v1/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse<UserCreateResponseDto>> createUser(@Valid @RequestBody UserCreateRequest req) {
        UserCreateResponseDto result = userService.create(req);

        SuccessResponse<UserCreateResponseDto> response = SuccessResponse.<UserCreateResponseDto>builder()
                .data(result)
                .message("회원 가입 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "회원 조회",
            description = "회원을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
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
    })
    @GetMapping("/api/v1/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse<UserDto>> findUser(@Parameter(description = "회원 id", required = true)
                                                              @PathVariable Long userId) {

        UserDto result = userService.findUser(userId);

        SuccessResponse<UserDto> response = SuccessResponse.<UserDto>builder()
                .data(result)
                .message("회원 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "회원 정보를 수정합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원 정보 수정 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = UserUpdateRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
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
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 이미 사용중인 닉네임입니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PutMapping("/api/v1/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse<UserDto>> update(@Parameter(description = "회원 id", required = true)
                                                          @PathVariable Long userId, @Valid @RequestBody UserUpdateRequest req) {
        UserDto result = userService.update(userId, req);

        SuccessResponse<UserDto> response = SuccessResponse.<UserDto>builder()
                .data(result)
                .message("회원 정보 수정 성공")
                .build();

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "회원 삭제",
            description = "회원을 삭제합니다."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDeleteResponseDto.class)
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
            )}
    )
    @DeleteMapping("/api/v1/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse<UserDeleteResponseDto>> delete(@Parameter(description = "회원 id", required = true)
                           @PathVariable Long userId) {
        UserDeleteResponseDto result = userService.delete(userId);

        SuccessResponse<UserDeleteResponseDto> response = SuccessResponse.<UserDeleteResponseDto>builder()
                .data(result)
                .message("회원 삭제 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}
