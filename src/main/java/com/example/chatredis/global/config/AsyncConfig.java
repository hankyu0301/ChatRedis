package com.example.chatredis.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync // 1
@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer{

    @Override
    public Executor getAsyncExecutor() { // 2
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.setThreadNamePrefix("async-thread-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {   // 3
        return (ex, method, params) -> log.info("exception occurred in {} {} : {}", method.getName(), params, ex.getMessage());
    }
}

/* *
 * 1 - Async 를 활성화
 * 2 - 스레드풀을 지정, 이를 지정해주지 않는다면, 비효율적인 디폴트 방식을 사용
 *     초기 스레드 개수(corePoolSize), corePoolSize 가 모두 사용 중일 때 새로 만들어지는 최대 스레드 개수(maxPoolSize),
 *     maxPoolSize 가 모두 사용 중일 때 대기하는 큐의 크기(queueCapacity), 스레드명의 접두어(threadNamePrefix)를 지정
 * 3 - 비동기 메소드에서 발생하는 예외는, @RestControllerAdvice 에서 잡아낼 수 없음
 * */
