package com.topcode.topenergyv2.authservice.user;


import com.topcode.topenergyv2.authservice.utils.APIResponse;
import com.topcode.topenergyv2.authservice.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;


@Component
public class UserHandler {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
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

    public Mono<ServerResponse> find(ServerRequest req){
        System.out.println(req.queryParams());
        Flux<User> users = userService.find(req.queryParams());

        return  users.map(u-> ObjectMapperUtils.map(u,UserDTO.class)).collectList()
                .flatMap(p->p.size() == 0 ? ServerResponse.status(404).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"No users found").getAsMap())) : ServerResponse.ok()
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


    public Mono<ServerResponse> findById(ServerRequest req){
        String userId = req.pathVariable("userId");
        Mono<User> user = userService.findById(userId);

        return user.map(u-> ObjectMapperUtils.map(u,UserDTO.class))
                .flatMap(u->{
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(new APIResponse<UserDTO>(u,true,"").getAsMap()));
                }).switchIfEmpty(ServerResponse.status(404).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"The user was not found").getAsMap())));
    }


    public Mono<ServerResponse> updateUser(ServerRequest req){
        String userId = req.pathVariable("userId");

        return req.bodyToMono(UserDTO.class)
                .flatMap(b->{
                    try {
                        return userService.updateUser(b,userId).map(u-> ObjectMapperUtils.map(u,UserDTO.class)).flatMap(u->{
                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(new APIResponse<UserDTO>(u,true,"").getAsMap()));
                        }).switchIfEmpty(ServerResponse.status(400).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"The was an error").getAsMap())));
                    } catch (IllegalAccessException e) {
                        return Mono.error(new RuntimeException(e));
                    }

                });
    }

    public Mono<ServerResponse> updateUserLogo(ServerRequest req){
        String userId = req.pathVariable("userId");


        return req.body(BodyExtractors.toMultipartData()).flatMap(b->{
            FilePart logo = (FilePart) b.toSingleValueMap().get("logo");
            try {
                return this.userService.updateUserLogo(logo,"users/"+ userId + "/logo",userId).flatMap(url->{
                    return ServerResponse.status(200).body(BodyInserters.fromValue(new APIResponse<String>(url,true,"").getAsMap()));
                }).switchIfEmpty(ServerResponse.status(404).body(BodyInserters.fromValue(new APIResponse<Void>(null,true,"There user was not found").getAsMap())));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        });

    }




}
