package com.example.chatredis.chat.controller;

import com.example.chatredis.domain.chat.controller.ChatRoomController;
import com.example.chatredis.domain.chat.dto.request.*;
import com.example.chatredis.domain.chat.dto.response.*;
import com.example.chatredis.domain.chat.service.ChatRoomService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoomService chatRoomService;

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("O 성공 회원의 모든 채팅방 조회")
    @Test
    @WithMockUser
    void getAllChatRoomByUserId() throws Exception {
        // Given
        List<SimpleChatRoomResponseDto> dtoList = new ArrayList<>();
        LongStream.rangeClosed(0,10).forEach(
                i -> dtoList.add(new SimpleChatRoomResponseDto("lastMessage", LocalDateTime.of(2024,3,18,0,0), "testChatRoom" + i, i)));
        ChatRoomListResponseDto result = new ChatRoomListResponseDto(dtoList);

        given(chatRoomService.getAllChatRoomByUserId(1L)).willReturn(result);

        // When
        mockMvc.perform(get("/api/v1/group/chatRoom/list/users/{userId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success":true,
                            "data":{
                                "simpleChatRoomResponseDtoList":[{
                                    "lastMessage":"lastMessage",
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
                                    "chatRoomId":10}]},
                                    
                                "message":"채팅방 목록 조회 성공"
                            }
                """));
    }

    @DisplayName("O 성공 채팅방 정보 상세 조회")
    @Test
    @WithMockUser
    void getChatRoom() throws Exception {
        // Given
        ChatRoomDto dto = ChatRoomDto.builder()
                .chatRoomId(1L)
                .chatRoomName("testChatRoom")
                .userDTOList(List.of(
                        UserDto.builder()
                                .id(1L)
                                .email("test1@test.email")
                                .nickname("testNickName1")
                                .username("testUsername1")
                                .build(),
                        UserDto.builder()
                                .id(2L)
                                .email("test2test.email")
                                .nickname("testNickName2")
                                .username("testUsername2")
                                .build()
                ))
                .build();

        given(chatRoomService.getChatRoomWithUserListByChatRoomId(1L, 1L)).willReturn(dto);

        // When
        mockMvc.perform(get("/api/v1/group/chatRoom/{chatRoomId}/users/{userId}", 1L, 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success":true,
                            "data":{
                                "chatRoomId":1,
                                "chatRoomName":"testChatRoom",
                                "userDTOList":[{
                                    "id":1,
                                    "email":"test1@test.email",
                                    "nickname":"testNickName1",
                                    "username":"testUsername1"},
                                    
                                    {"id":2,
                                    "email":"test2test.email",
                                    "nickname":"testNickName2",
                                    "username":"testUsername2"}]},
                                    
                                "message":"채팅방 정보 조회 성공"
                            }
                """));
    }

    @DisplayName("O 성공 채팅방 생성")
    @Test
    @WithMockUser
    void createChatRoom() throws Exception {
        // Given
        ChatRoomCreateRequest req = new ChatRoomCreateRequest(1L, List.of(2L,3L,4L), "testChatRoom");
        ChatRoomCreateResponseDto result = new ChatRoomCreateResponseDto(1L,"testChatRoom", List.of(2L,3L,4L) );

        given(chatRoomService.createChatRoom(any(ChatRoomCreateRequest.class))).willReturn(result);

        // When
        mockMvc.perform(post("/api/v1/group/chatRoom")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success":true,
                            "data":{
                                "chatRoomId":1,
                                "chatRoomName":"testChatRoom",
                                "userIdList":[2,3,4]},
                                
                            "message":"채팅방 생성 성공"
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

        given(chatRoomService.exitChatroom(anyLong(), any(ChatRoomDeleteRequest.class))).willReturn(result);

        // When
        mockMvc.perform(patch("/api/v1/group/chatRoom/{chatRoomId}/exit", chatRoomId)
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success":true,
                            "data":{
                                "chatRoomId":1
                                },
                            "message":"채팅방 퇴장 성공"}
                """));
    }

    @DisplayName("O 성공 채팅방 초대")
    @Test
    @WithMockUser
    void inviteChatRoom() throws Exception {
        // Given
        ChatRoomInviteRequest req = new ChatRoomInviteRequest(1L,2L, 1L);
        ChatRoomInviteResponseDto result = new ChatRoomInviteResponseDto(1L, 2L, 1L);

        given(chatRoomService.inviteChatRoom(any(ChatRoomInviteRequest.class))).willReturn(result);

        // When
        mockMvc.perform(post("/api/v1/group/chatRoom/invite")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"success":true,
                            "data":{
                                "inviterId":1,
                                "targetId":2,
                                "chatRoomId":1
                                },
                            "message":"채팅방 초대 성공"}
                """));
    }

    @DisplayName("O 성공 채팅방 초대 코드 생성")
    @Test
    @WithMockUser
    void createInviteCode() throws Exception {
        // Given
        ChatRoomInviteCodeCreateRequest req = new ChatRoomInviteCodeCreateRequest(1L, 2L);
        ChatRoomInviteCodeResponseDto result = new ChatRoomInviteCodeResponseDto(2L, "testInviteCode");

        given(chatRoomService.createInviteCode(any(ChatRoomInviteCodeCreateRequest.class))).willReturn(result);

        // When
        mockMvc.perform(post("/api/v1/group/chatRoom/inviteCode")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"success":true,
                            "data":{
                                "chatRoomId":2,
                                "inviteCode":"testInviteCode"
                                },
                            "message":"채팅방 초대 코드 생성 성공"}
                """));
    }

    @DisplayName("O 성공 채팅방 입장")
    @Test
    @WithMockUser
    void joinChatRoom() throws Exception {
        // Given
        ChatRoomJoinRequest req = new ChatRoomJoinRequest(1L, "testInviteCode");
        ChatRoomJoinResponseDto result = new ChatRoomJoinResponseDto(2L, "chatRoomName");

        given(chatRoomService.joinChatRoomByInviteCode(any(ChatRoomJoinRequest.class))).willReturn(result);

        // When
        mockMvc.perform(post("/api/v1/group/chatRoom/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"success":true,
                            "data":{
                                "chatRoomId":2,
                                "chatRoomName":"chatRoomName"
                                },
                            "message":"채팅방 입장 성공"}
                """));
    }
}
