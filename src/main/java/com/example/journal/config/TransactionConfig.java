package com.example.journal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    @Bean
    public PlatformTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
//        This bean tells Spring:
//
//        How to start a MongoDB transaction
//        How to commit when all operations succeed
//        How to rollback when any operation fails
        return new MongoTransactionManager(dbFactory);
    }
}

