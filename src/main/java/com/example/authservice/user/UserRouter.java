package com.example.authservice.user;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.context.annotation.Bean;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoute(UserHandler userHandler){
        return RouterFunctions.route()
                .path("/users", b -> b
                .GET("/all", accept(MediaType.APPLICATION_JSON),userHandler::findAll)
                .GET("/byUsername", accept(MediaType.APPLICATION_JSON),userHandler::findByUsername))
                .build();

    }
}


