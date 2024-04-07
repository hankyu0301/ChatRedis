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
@Schema(description = "채팅방 목록 응답")
public class ChatRoomListResponseDto {

    @Schema(description = "간단한 채팅방 정보 목록")
    private List<SimpleChatRoomResponseDto> simpleChatRoomResponseDtoList;
}
