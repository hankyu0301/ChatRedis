package com.example.chatredis.domain.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅방 생성 응답")
public class ChatRoomCreateResponseDto {

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "채팅방 이름", example = "채팅방1")
    private String chatRoomName;

    @Schema(description = "회원 ID 목록", example = "[1, 2, 3]")
    private List<Long> userIdList;
}