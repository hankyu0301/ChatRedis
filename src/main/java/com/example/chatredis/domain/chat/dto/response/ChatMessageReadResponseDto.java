package com.example.chatredis.domain.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅 메시지 읽기 응답")
public class ChatMessageReadResponseDto {

    @Schema(description = "메시지 개수")
    private int numberOfElements;

    @Schema(description = "다음 페이지 존재 여부")
    private boolean hasNext;

    @Schema(description = "채팅 메시지 리스트")
    private List<ChatMessageDto> chatMessageResponseDtoList;

    public static ChatMessageReadResponseDto toDto(Slice<ChatMessageDto> slice) {
        return new ChatMessageReadResponseDto(slice.getNumberOfElements(), slice.hasNext(), slice.getContent());
    }
}
