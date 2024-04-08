package com.example.chatredis.chat.controller;

import com.example.chatredis.domain.chat.controller.PrivateChatRoomController;
import com.example.chatredis.domain.chat.dto.request.ChatRoomDeleteRequest;
import com.example.chatredis.domain.chat.dto.response.*;
import com.example.chatredis.domain.chat.service.PrivateChatRoomService;
import com.example.chatredis.domain.user.dto.response.UserDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(PrivateChatRoomController.class)
class PrivateChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrivateChatRoomService privateChatRoomService;

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("O 성공 회원 채팅방 목록 조회")
    @Test
    @WithMockUser
    void getAllPrivateChatRoomByUserId() throws Exception {
        // Given
        List<SimpleChatRoomResponseDto> dtoList = new ArrayList<>();
        LongStream.rangeClosed(0,10).forEach(
                i -> dtoList.add(new SimpleChatRoomResponseDto("lastMessage", LocalDateTime.of(2024,3,18,0,0), "testChatRoom" + i, i)));
        ChatRoomListResponseDto result = new ChatRoomListResponseDto(dtoList);

        given(privateChatRoomService.getAllChatRoomByUserId(1L)).willReturn(result);

        // When
        mockMvc.perform(get("/api/v1/private/chatRoom/list/users/{userId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(
                        content().json("""
                        {"success":true,
                            "data":{
                                "simpleChatRoomResponseDtoList":[
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom0",
                                    "chatRoomId":0},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom1",
                                    "chatRoomId":1},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom2",
                                    "chatRoomId":2},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom3",
                                    "chatRoomId":3},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom4",
                                    "chatRoomId":4},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom5",
                                    "chatRoomId":5},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom6",
                                    "chatRoomId":6},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom7",
                                    "chatRoomId":7},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom8",
                                    "chatRoomId":8},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom9",
                                    "chatRoomId":9},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom10",
                                    "chatRoomId":10}
                                    ]},
                                    
                                "message":"채팅방 목록 조회 성공"}
                """));

    }

    @DisplayName("O 성공 채팅방 조회")
    @Test
    @WithMockUser
    void readChatRoom() throws Exception {
        // Given

        Long userId = 1L;
        Long chatRoomId = 1L;
        UserDto userdto = UserDto.builder().id(2L).email("test@test.com").nickname("testnickname").username("testusername").build();
        PrivateChatRoomDto result = PrivateChatRoomDto.builder().chatRoomName("test").chatRoomId(1L).userDto(userdto).build();

        given(privateChatRoomService.getChatRoomByIdWithUsers(anyLong(), anyLong())).willReturn(result);

        // When
        mockMvc.perform(get("/api/v1/private/chatRoom/{chatRoomId}/users/{userId}", chatRoomId, userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(
                        content().json("""
                        {
                          "success": true,
                          "data": {
                            "chatRoomId": 1,
                            "chatRoomName": "test",
                            "userDto": {
                                "id": 2,
                                "email": "test@test.com",
                                "nickname": "testnickname",
                                "username": "testusername"
                            }
                          },
                          "message": "채팅방 정보 조회 성공"
                        }
                """));
    }


    @DisplayName("O 성공 채팅방 퇴장")
    @Test
    @WithMockUser
    void exitChatRoom() throws Exception {
        // Given
        ChatRoomDeleteRequest req = new ChatRoomDeleteRequest(1L);
        ChatRoomDeleteResponseDto result = new ChatRoomDeleteResponseDto(1L);
        Long chatRoomId = 1L;

        given(privateChatRoomService.exitChatroom(anyLong(), any(ChatRoomDeleteRequest.class))).willReturn(result);

        // When
        mockMvc.perform(patch("/api/v1/private/chatRoom/{chatRoomId}/exit", chatRoomId)
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().json("""
                        {
                          "success": true,
                          "data": {
                            "chatRoomId": 1
                          },
                          "message": "채팅방 퇴장 성공"
                        }
                """));
    }

}
