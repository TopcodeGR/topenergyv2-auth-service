package com.example.authservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;


@SpringBootApplication
@EnableReactiveMongoRepositories
public class AuthServiceApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(AuthServiceApplication.class, args);
    }

}
