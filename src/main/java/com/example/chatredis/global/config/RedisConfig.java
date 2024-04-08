package com.example.chatredis.global.config;

import com.example.chatredis.domain.chat.service.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    /**
     * Chat Redis 설정
     * */
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    //그룹 채팅
    @Bean(name = "groupChannelTopic")
    public ChannelTopic groupChannelTopic() {
        return new ChannelTopic("groupChatRoom");
    }

    @Bean(name = "groupMessageListenerAdapter")
    public MessageListenerAdapter groupMessageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "sendGroupMessage");
    }

    @Bean(name = "redisGroupMessageListenerContainer")
    public RedisMessageListenerContainer redisGroupMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                            MessageListenerAdapter groupMessageListenerAdapter,
                                                                            ChannelTopic groupChannelTopic){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(groupMessageListenerAdapter, groupChannelTopic);
        return container;
    }

    //1:1 채팅
    @Bean(name = "privateChannelTopic")
    public ChannelTopic privateChannelTopic() {
        return new ChannelTopic("privateChatRoom");
    }

    @Bean(name = "privateMessageListenerAdapter")
    public MessageListenerAdapter privateMessageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "sendPrivateMessage");
    }

    @Bean(name = "redisPrivateMessageListenerContainer")
    public RedisMessageListenerContainer redisPrivateMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                              MessageListenerAdapter privateMessageListenerAdapter,
                                                              ChannelTopic privateChannelTopic){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(privateMessageListenerAdapter, privateChannelTopic);
        return container;
    }

    @Bean(name = "redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setPassword(password);
        return new LettuceConnectionFactory(configuration);
    }

}