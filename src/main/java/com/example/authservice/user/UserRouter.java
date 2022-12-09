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
    public RouterFunction<ServerResponse> route(UserHandler userHandler){
        return RouterFunctions.route()
                .path("/users",
                        b1 -> b1.nest(accept(MediaType.APPLICATION_JSON) ,
                                b2 -> b2.GET("/all",userHandler::findAll)))
                .build();

    }
}
