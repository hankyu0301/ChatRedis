package com.example.chatredis.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원 삭제 응답")
public class UserDeleteResponseDto {

    @Schema(description = "삭제된 회원 id", example = "1")
    private Long id;
}
