package com.example.authservice.user;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserRepository extends ReactiveMongoRepository<User,String> {
}
