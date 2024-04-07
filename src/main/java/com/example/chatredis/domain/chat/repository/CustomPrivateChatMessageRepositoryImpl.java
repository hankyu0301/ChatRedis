package com.example.chatredis.domain.chat.repository;

import com.example.chatredis.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.chatredis.domain.chat.dto.response.PrivateChatMessageDto;
import com.example.chatredis.domain.chat.entity.PrivateChatMessage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.example.chatredis.domain.chat.entity.QPrivateChatMessage.privateChatMessage;
import static com.example.chatredis.domain.chat.entity.QPrivateChatRoom.privateChatRoom;
import static com.querydsl.core.types.Projections.constructor;


public class CustomPrivateChatMessageRepositoryImpl extends QuerydslRepositorySupport implements CustomPrivateChatMessageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomPrivateChatMessageRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(PrivateChatMessage.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<PrivateChatMessageDto> findLatestMessages(ChatMessageReadCondition cond, List<Long> deletedMessageIds, Long limit) {
        Pageable pageable = PageRequest.ofSize(cond.getSize());
        Predicate predicate = createPredicate(cond, deletedMessageIds, limit);
        List<PrivateChatMessageDto> fetchResults = fetchResults(pageable, predicate);
        boolean hasNext = false;
        if (fetchResults.size() > pageable.getPageSize()) {
            fetchResults.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(fetchResults, pageable, hasNext);
    }

    private Predicate createPredicate(ChatMessageReadCondition cond, List<Long> deletedMessageIds, Long limit) {
        return new BooleanBuilder()
                .and(chatRoomIdExpression(cond))
                .and(deletedMessageIdsExpression(deletedMessageIds))
                .and(lastMessageIdExpression(cond))
                .and(limitExpression(limit));
    }

    private BooleanExpression chatRoomIdExpression(ChatMessageReadCondition cond) {
        return privateChatMessage.privateChatRoom.id.eq(cond.getChatRoomId());
    }

    private BooleanExpression deletedMessageIdsExpression(List<Long> deletedMessageIds) {
        return deletedMessageIds.isEmpty() ? null : privateChatMessage.id.notIn(deletedMessageIds);
    }

    private BooleanExpression lastMessageIdExpression(ChatMessageReadCondition cond) {
        return privateChatMessage.id.lt(cond.getLastChatMessageId());
    }

    private BooleanExpression limitExpression(Long limit) {
        return privateChatMessage.id.goe(limit);
    }

    private List<PrivateChatMessageDto> fetchResults(Pageable pageable, Predicate predicate) {
        return jpaQueryFactory
                .select(constructor(PrivateChatMessageDto.class, privateChatMessage.id, privateChatMessage.privateChatRoom.id, privateChatMessage.userId, privateChatMessage.username, privateChatMessage.content, privateChatMessage.createdAt))
                .from(privateChatMessage)
                .join(privateChatMessage.privateChatRoom, privateChatRoom)
                .where(predicate)
                .orderBy(privateChatMessage.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }
}
