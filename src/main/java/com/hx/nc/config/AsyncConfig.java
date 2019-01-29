package com.hx.nc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author XingJiajun
 * @Date 2019/1/9 10:06
 * @Description
 */
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    private TaskExecutorBuilder taskExecutorBuilder;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public AsyncConfig(TaskExecutorBuilder builder,
                       @Qualifier("applicationTaskExecutor") ThreadPoolTaskExecutor executor) {
        taskExecutorBuilder = builder;
        threadPoolTaskExecutor = executor;
    }


    @Override
    public Executor getAsyncExecutor() {
        return taskExecutorBuilder.configure(threadPoolTaskExecutor);
    }

//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = executor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(50);
//        executor.setKeepAliveSeconds(10);
//        executor.initialize();
//        return executor;
//    }
//
//    @Bean
//    public ThreadPoolTaskExecutor executor() {
//        return new ThreadPoolTaskExecutor();
//    }
}
