package com.example.authservice.user;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface IUserRepository extends ReactiveMongoRepository<User,String> {


    Mono<User> findByUsername(String username);
}
