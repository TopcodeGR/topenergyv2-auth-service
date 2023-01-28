package com.topcode.topenergyv2.authservice.user;


import com.topcode.topenergyv2.authservice.client.ClientService;
import com.topcode.topenergyv2.authservice.filters.ClientHeadersFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.context.annotation.Bean;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class UserRouter {

    @Autowired
    private ClientService clientService;

    @Bean
    public RouterFunction<ServerResponse> userRoute(UserHandler userHandler){
        return RouterFunctions.route()
                .path("/users", b -> b
                        .GET("/", accept(MediaType.APPLICATION_JSON),userHandler::find)
                .GET("/all", accept(MediaType.APPLICATION_JSON),userHandler::findAll)
                .GET("/byUsername", accept(MediaType.APPLICATION_JSON),userHandler::findByUsername)
                .GET("/{userId}", accept(MediaType.APPLICATION_JSON),userHandler::findById)
                .PUT("/{userId}", accept(MediaType.APPLICATION_JSON),userHandler::updateUser)
                        .PUT("/{userId}/logo", accept(MediaType.MULTIPART_FORM_DATA),userHandler::updateUserLogo))
                .filter(new ClientHeadersFilter(clientService))
                .build();

    }
}


