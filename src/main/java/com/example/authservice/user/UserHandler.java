package com.example.authservice.user;


import com.example.authservice.utils.APIResponse;
import com.example.authservice.utils.ObjectMapperUtils;
import com.mongodb.internal.connection.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Component
public class UserHandler {

    @Autowired
    private IUserRepository userRepository;

    private UserService userService;

    private Mono<ServerResponse> response404
            = ServerResponse.notFound().build();


    public Mono<ServerResponse> findAll(ServerRequest req){
        Flux<User> users = userService.findAll();


        return  users.map(u-> ObjectMapperUtils.map(u,UserDTO.class)).collectList()
                .flatMap(p->p.size() == 0 ? ServerResponse.status(400).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"Something went wrong").getAsMap())) : ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(new APIResponse<List<UserDTO>>(p,true,"").getAsMap())));
    }


    public Mono<ServerResponse> findByUsername(ServerRequest req){

        Optional<String> usernameParam = req.queryParam("username");
        if(!usernameParam.isPresent()){
            return ServerResponse.status(400).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"Please provide a username").getAsMap()));
        }

        String username = usernameParam.get();
        Mono<User> user = userService.findByUsername(username);

        return user.map(u-> ObjectMapperUtils.map(u,UserDTO.class))
                .flatMap(u-> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(new APIResponse<UserDTO>(u,true,"").getAsMap())))
                .switchIfEmpty(ServerResponse.status(404).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"The user was not found").getAsMap())));

    }

}
