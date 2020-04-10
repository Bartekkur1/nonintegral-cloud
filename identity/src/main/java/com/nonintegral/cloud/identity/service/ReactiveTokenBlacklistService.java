package com.nonintegral.cloud.identity.service;

import reactor.core.publisher.Mono;

public interface ReactiveTokenBlacklistService {

    Mono<Void> BlacklistToken(String token);
    Mono<Boolean> isTokenBlacklisted(String token);

}
