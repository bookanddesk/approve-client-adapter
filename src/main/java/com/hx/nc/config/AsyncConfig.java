//package com.hx.nc.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Executor;
//
///**
// * @author XingJiajun
// * @Date 2019/1/9 10:06
// * @Description
// */
//@Configuration
//
//public class AsyncConfig implements AsyncConfigurer {
//
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
//}
