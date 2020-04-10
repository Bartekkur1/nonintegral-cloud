package com.nonintegral.cloud.identity.security;

import com.nonintegral.cloud.identity.entity.User;
import reactor.core.publisher.Mono;

public interface ReactiveAuthenticationFacade {
    Mono<User> getUser();
}
