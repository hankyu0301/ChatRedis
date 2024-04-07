package com.example.chatredis.domain.chat.repository;

import com.example.chatredis.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.chatredis.domain.chat.dto.response.ChatMessageDto;
import com.example.chatredis.domain.chat.entity.ChatMessage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.chatredis.domain.chat.entity.QChatMessage.chatMessage;
import static com.example.chatredis.domain.chat.entity.QChatRoomUser.chatRoomUser;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.jpa.JPAExpressions.select;

@Transactional
public class CustomChatMessageRepositoryImpl extends QuerydslRepositorySupport implements CustomChatMessageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomChatMessageRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(ChatMessage.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<ChatMessageDto> findLatestMessages(List<Long> deletedMessageIds, ChatMessageReadCondition cond) {
        Pageable pageable = PageRequest.ofSize(cond.getSize());
        Predicate predicate = createPredicate(cond, deletedMessageIds);
        List<ChatMessageDto> fetchResults = fetchResults(pageable, predicate);
        boolean hasNext = false;
        if (fetchResults.size() > pageable.getPageSize()) {
            fetchResults.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(fetchResults, pageable, hasNext);
    }

    private Predicate createPredicate(ChatMessageReadCondition cond, List<Long> deletedMessageIds) {
        return new BooleanBuilder()
                .and(chatRoomIdExpression(cond))
                .and(deletedMessageIdsExpression(deletedMessageIds))
                .and(lastMessageIdExpression(cond))
                .and(firstMessageIdExpression(cond));
    }

    private BooleanExpression chatRoomIdExpression(ChatMessageReadCondition cond) {
        return chatMessage.chatRoom.id.eq(cond.getChatRoomId());
    }

    private BooleanExpression lastMessageIdExpression(ChatMessageReadCondition cond) {
        return chatMessage.id.lt(cond.getLastChatMessageId());
    }

    private BooleanExpression deletedMessageIdsExpression(List<Long> deletedMessageIds) {
        return deletedMessageIds.isEmpty() ? null : chatMessage.id.notIn(deletedMessageIds);
    }

    private BooleanExpression firstMessageIdExpression(ChatMessageReadCondition cond) {
        return chatMessage.id.goe(
                    select(chatRoomUser.firstMessageId)
                    .from(chatRoomUser)
                    .where(chatRoomUser.user.id.eq(cond.getUserId())
                        .and(chatRoomUser.chatRoom.id.eq(cond.getChatRoomId())))
        );
    }
    private List<ChatMessageDto> fetchResults(Pageable pageable, Predicate predicate) {

        return jpaQueryFactory
                .select(constructor(
                        ChatMessageDto.class,
                        chatMessage.id,
                        chatMessage.chatRoom.id,
                        chatMessage.chatRoom.chatRoomName,
                        chatMessage.userId,
                        chatMessage.username,
                        chatMessage.content,
                        chatMessage.type,
                        chatMessage.createdAt))
                .from(chatMessage)
                .where(predicate)
                .orderBy(chatMessage.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }
}
