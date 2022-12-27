package com.example.authservice.auth;


import com.example.authservice.client.ClientService;
import com.example.authservice.filters.ClientHeadersFilter;
import com.example.authservice.user.UserHandler;
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
public class AuthRouter {

    @Autowired
    private ClientService clientService;

    @Bean
    public RouterFunction<ServerResponse> authRoute(AuthHandler authHandler){
        return RouterFunctions.route()
                .path("/auth", b -> b
                        .POST("/login", accept(MediaType.APPLICATION_JSON),authHandler::login)
                        .POST("/authorizeRequest", accept(MediaType.APPLICATION_JSON),authHandler::authorizeRequest)
                        .POST("/renew", accept(MediaType.APPLICATION_JSON),authHandler::renewAuth))
                .filter(new ClientHeadersFilter(clientService))
                .build();

    }
}


