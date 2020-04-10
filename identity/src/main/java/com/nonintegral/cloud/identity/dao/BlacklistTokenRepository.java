package com.nonintegral.cloud.identity.dao;

import com.nonintegral.cloud.identity.entity.BlacklistToken;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BlacklistTokenRepository extends ReactiveMongoRepository<BlacklistToken, String> {
    Mono<Boolean> existsByToken(String token);
}
