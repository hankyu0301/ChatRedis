package com.example.chatredis.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅방 입장 요청")
public class ChatRoomJoinRequest {

    @NotNull(message = "회원 ID는 필수 입력 값입니다.")
    @Schema(description = "회원 ID", example = "1")
    private Long userId;

    @NotBlank(message = "초대 코드는 필수 입력 값입니다.")
    @Schema(description = "초대 코드", example = "1234")
    private String inviteCode;
}
