package com.example.authservice.user;


import com.example.authservice.utils.APIResponse;
import com.example.authservice.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;


@Component
public class UserHandler {

    @Autowired
    private IUserRepository userRepository;
    private Mono<ServerResponse> response404
            = ServerResponse.notFound().build();


    public Mono<ServerResponse> findAll(ServerRequest req){
        Flux<User> users = userRepository.findAll();


        return  users.map(u-> ObjectMapperUtils.map(u,UserDTO.class)).collectList()
                .flatMap(p->p.size() == 0 ? ServerResponse.status(400).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"Something went wrong").getAsMap())) : ServerResponse.ok()
                .contentType(MediaType.APPLICATION_XHTML_XML)
                        .body(BodyInserters.fromValue(new APIResponse<List<UserDTO>>(p,true,"").getAsMap())));
    }

}
