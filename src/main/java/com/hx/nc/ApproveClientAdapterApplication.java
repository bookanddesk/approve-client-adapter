package com.hx.nc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ApproveClientAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApproveClientAdapterApplication.class, args);
    }

//    @Configuration
//    static class LazyJpaConfiguration {
//
//        @Bean
//        public LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaVendorAdapter jpaVendorAdapter,
//                                                                           DataSource dataSource, Environment environment) {
//
//            ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//            threadPoolTaskExecutor.setDaemon(true);
//            threadPoolTaskExecutor.afterPropertiesSet();
//
//            LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//            emf.setBootstrapExecutor(threadPoolTaskExecutor);
//            emf.setDataSource(dataSource);
//            emf.setJpaVendorAdapter(jpaVendorAdapter);
//            emf.setPackagesToScan(this.getClass().getPackage().getName());
//
//            return emf;
//        }
//    }
//
//    @Configuration
//    @EnableJpaRepositories(bootstrapMode = BootstrapMode.DEFERRED)
//    static class LazyRepositoryConfiguration {}


}
