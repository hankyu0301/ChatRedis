package com.example.chatredis.chat.controller;

import com.example.chatredis.domain.chat.controller.ChatMessageController;
import com.example.chatredis.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.chatredis.domain.chat.dto.response.ChatMessageDto;
import com.example.chatredis.domain.chat.dto.response.ChatMessageReadResponseDto;
import com.example.chatredis.domain.chat.entity.ChatMessage;
import com.example.chatredis.domain.chat.service.ChatMessageService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(ChatMessageController.class)
class ChatMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService chatMessageService;

    @DisplayName("O 성공 최근 메시지 조회")
    @Test
    @WithMockUser
    void getMessageList() throws Exception {
        // Given
        ChatMessageReadCondition cond = new ChatMessageReadCondition(1L, 1L, 0L, 20);
        ChatMessageReadResponseDto result = new ChatMessageReadResponseDto(1, false, List.of(
                ChatMessageDto.builder()
                        .chatMessageId(1L)
                        .chatRoomId(1L)
                        .chatRoomName("testName")
                        .username("testUsername")
                        .userId(1L)
                        .content("testContent")
                        .type(ChatMessage.MessageType.TALK)
                        .createdAt(LocalDateTime.of(2024,3,18,0,0))
                        .build()
        ));

        given(chatMessageService.findLatestMessage(any(ChatMessageReadCondition.class))).willReturn(result);

        // When
        mockMvc.perform(get("/api/v1/group/chat/message")
                        .param("userId", String.valueOf(cond.getUserId()))
                        .param("size", String.valueOf(cond.getSize()))
                        .param("chatRoomId", String.valueOf(cond.getChatRoomId()))
                        .param("lastMessageId", String.valueOf(cond.getLastChatMessageId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(
                content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"success":true,
                            "data":{
                                "numberOfElements":1,
                                "hasNext":false,
                                "chatMessageResponseDtoList":[{
                                    "chatMessageId":1,
                                    "chatRoomId":1,
                                    "chatRoomName":"testName",
                                    "userId":1,
                                    "username":"testUsername",
                                    "content":"testContent",
                                    "type":"TALK",
                                    "createdAt":"2024-03-18T00:00:00"}
                                    ]},
                                "message":"채팅 메시지 목록 조회 성공"}
                """));
    }
}
