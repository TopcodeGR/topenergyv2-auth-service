package com.topcode.topenergyv2.authservice.user;

import com.topcode.topenergyv2.authservice.utils.MongoUpdateUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class UserService {


    @Autowired
    private IUserRepository userRepository;

    @Autowired
    ReactiveMongoTemplate template;


    public Flux<User> findAll(){
        return userRepository.findAll();
    }

    public Mono<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public Mono<User> findById(String userId){
        return userRepository.findById(userId);
    }

    public Mono<User> updateUser(UserDTO updatedUser,String userId) throws IllegalAccessException {

        MongoUpdateUtils<UserDTO> updateUtils = new MongoUpdateUtils<UserDTO>(updatedUser);
        Update update = updateUtils.generateUpdate();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id")
                .is(new ObjectId(userId)));
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);

        return template.findAndModify(query,update,findAndModifyOptions,User.class);
    }

}
