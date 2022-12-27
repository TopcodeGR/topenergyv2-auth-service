package com.example.authservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class UserService {


    @Autowired
    private IUserRepository userRepository;

    public Flux<User> findAll(){
        return userRepository.findAll();
    }

    public Mono<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public Mono<User> findById(String userId){
        return userRepository.findById(userId);
    }

}
