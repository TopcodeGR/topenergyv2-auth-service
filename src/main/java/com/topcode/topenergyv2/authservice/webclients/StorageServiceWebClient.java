package com.topcode.topenergyv2.authservice.webclients;

import com.topcode.topenergyv2.authservice.utils.APIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;



@Component
public class StorageServiceWebClient {


    private final WebClient client;


    public StorageServiceWebClient(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://storage-service").build();
    }

    public Mono<String> upload(FilePart file,String uploadPath){

        LinkedMultiValueMap<String, Object> data = new LinkedMultiValueMap();
        data.add("file",file);
        data.add("path",uploadPath);
        return this.client.post().uri("/storage/upload").contentType(MediaType.MULTIPART_FORM_DATA).body(BodyInserters.fromMultipartData(data)).exchangeToMono(res->{
            return res.bodyToMono(new ParameterizedTypeReference<APIResponse<String>>(){}).map(r->{
                return r.getData();
            });
        });
    }
}
