package com.example.chatredis.domain.user.dto.response;

import com.example.chatredis.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "회원 정보 응답")
public class UserDto {

    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "회원 email", example = "finebears@naver.com")
    private String email;

    @Schema(description = "회원 닉네임", example = "finebears")
    private String nickname;

    @Schema(description = "회원 이름", example = "한규")
    private String username;

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUsername());
    }

    public static UserDto empty() {
        return new UserDto(null, "", "", "");
    }
}
