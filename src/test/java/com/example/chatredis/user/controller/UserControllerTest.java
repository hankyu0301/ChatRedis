package com.example.chatredis.user.controller;

import com.example.chatredis.domain.user.controller.UserController;
import com.example.chatredis.domain.user.dto.request.UserCreateRequest;
import com.example.chatredis.domain.user.dto.request.UserUpdateRequest;
import com.example.chatredis.domain.user.dto.response.UserCreateResponseDto;
import com.example.chatredis.domain.user.dto.response.UserDeleteResponseDto;
import com.example.chatredis.domain.user.dto.response.UserDto;
import com.example.chatredis.domain.user.entity.User;
import com.example.chatredis.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RequiredArgsConstructor
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("O 성공 회원 가입")
    @Test
    @WithMockUser
    void create_Success() throws Exception {
        //given
        UserCreateRequest req = new UserCreateRequest("email@email.com", "123456a!", "tsetUsername", "tsetNickname");
        given(userService.create(req)).willReturn(new UserCreateResponseDto(1L));

        mockMvc.perform(post("/api/v1/users").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success":true,
                                "data":{
                                    "id":1
                                },
                                "message":"회원 가입 성공"
                        }
                """));

        verify(userService, times(1)).create(req);
    }

    @DisplayName("O 성공 회원 조회")
    @Test
    @WithMockUser
    void findMember_Success() throws Exception {

        UserDto userDto = UserDto.toDto(new User("email@email.com", "password", "username", "nickname"));
        userDto.setId(1L);
        given(userService.findUser(userDto.getId())).willReturn(userDto);

        mockMvc.perform(get("/api/v1/users/{userId}", userDto.getId()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"success":true,
                            "data":{
                                "id":1,
                                "email":"email@email.com",
                                "username":"username",
                                "nickname":"nickname"
                            },
                                "message":"회원 조회 성공"}
                """)); // Assuming data field exists in the response

        verify(userService, times(1)).findUser(userDto.getId());
    }

    @DisplayName("O 성공 회원 수정")
    @Test
    @WithMockUser
    void update_Success() throws Exception {
        UserUpdateRequest req = new UserUpdateRequest("newUsername", "newNickname" );
        UserDto userDto = UserDto.toDto(new User("email@email.com", "password", "username", "nickname"));
        userDto.setId(1L);

        given(userService.update(userDto.getId(), req)).willReturn(userDto);

        mockMvc.perform(put("/api/v1/users/{userId}", userDto.getId()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"success":true,
                            "data":{
                                "id":1,
                                "email":"email@email.com",
                                "username":"username",
                                "nickname":"nickname"
                            },
                                "message":"회원 정보 수정 성공"}
                """));

        verify(userService, times(1)).update(userDto.getId(), req);
    }

    @DisplayName("O 성공 회원 삭제")
    @Test
    @WithMockUser
    void delete_Success() throws Exception {
        Long userId = 1L;
        UserDeleteResponseDto dto = new UserDeleteResponseDto(1L);
        given(userService.delete(userId)).willReturn(dto);

        mockMvc.perform(delete("/api/v1/users/{userId}", userId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"success":true,
                            "data":{
                                "id":1
                                },
                                "message":"회원 삭제 성공"}
                """));

        verify(userService, times(1)).delete(dto.getId());
    }
}
