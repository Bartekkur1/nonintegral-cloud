package com.nonintegral.cloud.identity.dao;

import com.nonintegral.cloud.identity.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByEmail(String email);
    Mono<User> findByUtilToken(String token);
    Mono<Boolean> existsByEmail(String email);
}