package com.nonintegral.cloud.identity.service;

import com.nonintegral.cloud.identity.entity.Credentials;
import com.nonintegral.cloud.identity.entity.User;
import reactor.core.publisher.Mono;

public interface ReactiveAuthService {

    Mono<String> login(Credentials credentials);
    Mono<String> jwtAuthenticate(Credentials credentials, User user);

}
