package com.example.chatredis.domain.chat.repository;

import com.example.chatredis.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.chatredis.domain.chat.dto.response.PrivateChatMessageDto;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomPrivateChatMessageRepository {

    Slice<PrivateChatMessageDto> findLatestMessages(ChatMessageReadCondition cond, List<Long> deletedMessageIds, Long limit);
}
