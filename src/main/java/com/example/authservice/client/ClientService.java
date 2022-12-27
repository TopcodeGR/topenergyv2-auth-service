package com.example.authservice.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ClientService {

    @Autowired
    IClientRepository clientRepository;

    public Mono<Boolean> isClientValid(String clientId, String clientSecret){
        Mono<Client> client = clientRepository.findByClientId(clientId);
        return client.map(c->{
            if(!c.getClientSecret().equals(clientSecret)){
                return false;
            }
            return true;
        }).switchIfEmpty(Mono.just(false));

    }
}
