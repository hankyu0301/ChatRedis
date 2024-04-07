package com.example.chatredis.domain.chat.repository;

import com.example.chatredis.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.chatredis.domain.chat.dto.response.ChatMessageDto;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomChatMessageRepository {

    Slice<ChatMessageDto> findLatestMessages(List<Long> deletedMessageIds, ChatMessageReadCondition cond);
}
