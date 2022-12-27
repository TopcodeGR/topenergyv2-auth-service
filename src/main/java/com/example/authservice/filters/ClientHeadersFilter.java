package com.example.authservice.filters;

import com.example.authservice.client.ClientService;
import com.example.authservice.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ClientHeadersFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    private final ClientService clientService;


    public ClientHeadersFilter(ClientService clientService){
        this.clientService = clientService;
    }
    @Override
    public Mono<ServerResponse> filter(ServerRequest req,
                                       HandlerFunction<ServerResponse> handlerFunction) {

        ServerRequest.Headers headers = req.headers();
        String clientId = headers.firstHeader("clientId");
        String clientSecret = headers.firstHeader("clientSecret");

        if(clientId == null || clientSecret == null || clientId.isBlank() || clientSecret.isBlank()) {
            return ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<Void>(null,false,"Please provide client headers.").getAsMap()));
        }

        return this.clientService.isClientValid(clientId,clientSecret).flatMap(isClientValid-> {
            if (!isClientValid) {
                return ServerResponse.status(401).body(BodyInserters.fromValue(new APIResponse<Void>(null, false, "You are not authorized to login from this client").getAsMap()));
            }
            return handlerFunction.handle(req);
        });

    }
}
