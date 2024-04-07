package com.example.chatredis.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅방 초대 코드 생성 요청")
public class ChatRoomInviteCodeCreateRequest {

    @NotNull(message = "초대 코드를 생성할 유저 ID는 필수 입력 값입니다.")
    @Positive(message = "올바른 유저 ID를 입력해주세요.")
    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    @NotNull(message = "초대 코드를 생성할 채팅방 ID는 필수 입력 값입니다.")
    @Positive(message = "올바른 채팅방 ID를 입력해주세요.")
    @Schema(description = "초대 코드를 생성할 채팅방 ID", example = "1")
    private Long chatRoomId;

}
