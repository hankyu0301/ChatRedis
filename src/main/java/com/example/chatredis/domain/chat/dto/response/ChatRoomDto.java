package com.example.chatredis.domain.chat.dto.response;

import com.example.chatredis.domain.user.dto.response.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅방 정보 응답")
public class ChatRoomDto {

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "채팅방 이름", example = "채팅방1")
    private String chatRoomName;

    @Schema(description = "회원 정보 목록")
    private List<UserDto> userDTOList;
}
