package com.example.authservice.auth;


import com.example.authservice.client.ClientHeaders;
import com.example.authservice.client.ClientService;
import com.example.authservice.user.IUserRepository;
import com.example.authservice.user.User;
import com.example.authservice.user.UserService;
import com.example.authservice.user.UserTokens;
import com.example.authservice.utils.APIResponse;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
@Component
public class AuthHandler {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    ReactiveMongoTemplate template;



    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    public Mono<ServerResponse> login(ServerRequest req){
        ServerRequest.Headers headers = req.headers();
        String clientId = headers.firstHeader("clientId");
        String clientSecret = headers.firstHeader("clientSecret");


        return req.bodyToMono(LoginBody.class)
                .flatMap(body->{
                    if(body.password == null || body.username == null || body.username.isBlank() || body.password.isBlank()  ){
                        return ServerResponse.status(400).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"Please provide your credentials.").getAsMap()));
                    }
                    Mono<User> user = userService.findByUsername(body.username);
                    return user.flatMap(u->{
                                Boolean isPasswordValid = authService.validateUserPassword(u.getPassword(),body.password);

                                if(!isPasswordValid){
                                    return ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"Your password is invalid.").getAsMap()));
                                }


                                return authService.updateUserTokens(u,clientId).flatMap(r->{
                                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                            .body(BodyInserters.fromValue(
                                                    new APIResponse<LoginResponse>(new LoginResponse(r.getAccessTokens().get(clientId).getToken(),r.getRefreshTokens().get(clientId).getToken(),u.get_id()),true,"").getAsMap()
                                            ));
                                });

                            })
                            .switchIfEmpty(ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"No user found with this username.").getAsMap())));

                })
                .switchIfEmpty(ServerResponse.status(400).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"Please provide your credentials.").getAsMap())));



    }

    public Mono<ServerResponse> authorizeRequest(ServerRequest req){
        ServerRequest.Headers headers = req.headers();
        String accessToken = headers.firstHeader("Authorization");

        if(accessToken == null){
            return ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"No authorization header is present").getAsMap()));
        }
        accessToken = accessToken.replaceAll("Bearer","").trim();
        String finalAccessToken = accessToken;

        Boolean isAccessTokenValid = authService.validateJWTToken(finalAccessToken);

        if(!isAccessTokenValid){
            return ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"Not authorized.").getAsMap()));
        }

        return ServerResponse.status(200).body(BodyInserters.fromValue(new APIResponse<Void>(null,true,"").getAsMap()));

    }


    public Mono<ServerResponse> renewAuth(ServerRequest req){
        ServerRequest.Headers headers = req.headers();
        String refreshToken = headers.firstHeader("refreshToken");
        String clientId = headers.firstHeader("clientId");
        String userId = authService.getUserIdFromToken(refreshToken);

        if(userId == null || userId.isBlank() ){
            return ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"This user is not authorized.").getAsMap()));
        }

        if(refreshToken == null || refreshToken.isBlank()){
            return ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"No refresh token was provided.").getAsMap()));
        }
        Boolean isRefreshTokenValid = authService.validateJWTToken(refreshToken);

        if(!isRefreshTokenValid){
            return ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"Not authorized.").getAsMap()));
        }

        userId = userId.replaceAll("^\"|\"$", "");
        String finalUserId = userId;
        return userRepository.findById(userId).flatMap(u->{

            return authService.updateUserTokens(u,clientId).flatMap(r->{

                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(
                                new APIResponse<LoginResponse>(new LoginResponse(r.getAccessTokens().get(clientId).getToken(),r.getRefreshTokens().get(clientId).getToken(),u.get_id()),true,"").getAsMap()
                        ));
            });

        })  .switchIfEmpty(ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<String>(finalUserId,false,"Wrong user.").getAsMap())));

    }







}
