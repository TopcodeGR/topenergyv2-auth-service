package com.topcode.topenergyv2.authservice.user;

import com.topcode.topenergyv2.authservice.utils.APIResponse;
import com.topcode.topenergyv2.authservice.utils.MongoUpdateUtils;
import com.topcode.topenergyv2.authservice.utils.ObjectMapperUtils;
import com.topcode.topenergyv2.authservice.webclients.StorageServiceWebClient;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class UserService {


    @Autowired
    private IUserRepository userRepository;

    @Autowired
    ReactiveMongoTemplate template;


    private  StorageServiceWebClient storageServiceClient;

    public UserService(StorageServiceWebClient storageServiceClientBuilder) {
        this.storageServiceClient = storageServiceClientBuilder;
    }

    private Query serializeQueryParamsToMongoQuery(MultiValueMap<String,String> queryParams){
        Query query = new Query();
        if(queryParams.get("stripeCustomerId") != null){
            System.out.println(queryParams.get("stripeCustomerId").get(0));
            query.addCriteria(Criteria.where("stripeCustomerId")
                    .is(queryParams.get("stripeCustomerId").get(0) ));
        }

        return query;
    }

    public Flux<User> findAll(){
        return userRepository.findAll();
    }

    public Flux<User> find(MultiValueMap<String,String> queryParams){
        Query query = this.serializeQueryParamsToMongoQuery(queryParams);
        return this.template.find(query,User.class);
    }

    public Mono<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public Mono<User> findById(String userId){
        return userRepository.findById(userId);
    }

    public Mono<User> updateUser(UserDTO updatedUser, String userId) throws IllegalAccessException {
        MongoUpdateUtils<UserDTO> updateUtils = new MongoUpdateUtils<UserDTO>(updatedUser);
        Update update = updateUtils.generateUpdate();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id")
                .is(new ObjectId(userId)));
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);

        return template.findAndModify(query,update,findAndModifyOptions,User.class);
    }
    public Mono<String> updateUserLogo(FilePart logo, String uploadPath, String userId) throws IllegalAccessException {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id")
                .is(new ObjectId(userId)));
        return template.findOne(query,User.class).flatMap(user->{
            return this.storageServiceClient.upload(logo,uploadPath).flatMap(url->{
                UserDTO updatedUser = ObjectMapperUtils.map(user,UserDTO.class);
                updatedUser.setLogo(url);
                MongoUpdateUtils<UserDTO> updateUtils = new MongoUpdateUtils<UserDTO>(updatedUser);
                FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(false);
                Update update = null;
                try {
                    update = updateUtils.generateUpdate();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return template.findAndModify(query,update,findAndModifyOptions,User.class).then(Mono.just(url));

            });

        }).switchIfEmpty(Mono.empty());
    }

}
