package com.example.authservice.user;


import com.example.authservice.client.ClientService;
import com.example.authservice.filters.ClientHeadersFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ClientService clientService;

    @Bean
    public RouterFunction<ServerResponse> userRoute(UserHandler userHandler){
        return RouterFunctions.route()
                .path("/users", b -> b
                .GET("/all", accept(MediaType.APPLICATION_JSON),userHandler::findAll)
                .GET("/byUsername", accept(MediaType.APPLICATION_JSON),userHandler::findByUsername)
                .GET("/{userId}", accept(MediaType.APPLICATION_JSON),userHandler::findById)
                .PUT("/{userId}", accept(MediaType.APPLICATION_JSON),userHandler::updateUser))
                .filter(new ClientHeadersFilter(clientService))
                .build();

    }
}


