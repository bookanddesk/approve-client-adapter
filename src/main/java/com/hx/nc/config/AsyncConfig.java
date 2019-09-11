package com.hx.nc.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author XingJiajun
 * @Date 2019/1/9 10:06
 * @Description
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

//    private TaskExecutorBuilder taskExecutorBuilder;
////    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
////
////    @Autowired
////    public AsyncConfig(TaskExecutorBuilder builder,
////                       @Qualifier("applicationTaskExecutor") ThreadPoolTaskExecutor executor) {
////        taskExecutorBuilder = builder;
////        threadPoolTaskExecutor = executor;
////    }


//    @Override
//    public Executor getAsyncExecutor() {
//        return taskExecutorBuilder.configure(threadPoolTaskExecutor);
//    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = executor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("acaAsync-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Bean
    public ThreadPoolTaskExecutor executor() {
        return new ThreadPoolTaskExecutor();
    }
}
