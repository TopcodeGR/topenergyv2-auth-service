package com.topcode.topenergyv2.authservice.client;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IClientRepository extends ReactiveMongoRepository<Client,String> {

    Mono<Client> findByClientId(String clientId);
}
