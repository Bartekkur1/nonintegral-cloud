package com.nonintegral.cloud.identity.service;

import com.nonintegral.cloud.identity.dao.BlacklistTokenRepository;
import com.nonintegral.cloud.identity.entity.BlacklistToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class BaseTokenBlacklistService implements ReactiveTokenBlacklistService {

    @Autowired
    private final BlacklistTokenRepository tokenRepository;

    public Mono<Void> BlacklistToken(String token) {
        return tokenRepository.save(new BlacklistToken(token))
            .flatMap(u -> Mono.empty());
    }

    public Mono<Boolean> isTokenBlacklisted(String token) {
        return tokenRepository.existsByToken(token);
    }
}
