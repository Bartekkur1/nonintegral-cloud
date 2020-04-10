package com.nonintegral.cloud.identity.service;

import com.nonintegral.cloud.identity.dao.UserRepository;
import com.nonintegral.cloud.identity.entity.Credentials;
import com.nonintegral.cloud.identity.entity.IdentityException;
import com.nonintegral.cloud.identity.entity.User;
import com.nonintegral.cloud.identity.entity.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ReactiveAuthServiceBase implements ReactiveAuthService {

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final ReactiveUserService userService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public Mono<String> login(Credentials credentials) {
        return userRepository.findByEmail(credentials.getEmail())
                .switchIfEmpty(Mono.error(new IdentityException(ResponseMessage.INVALID_PASSWORD)))
                .flatMap(u -> jwtAuthenticate(credentials, u));
    }

    public Mono<String> jwtAuthenticate(Credentials credentials, User user) {
        if(passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            return userService.updateUserLastLogin(user)
                    .map(jwtService::generateToken);
        }
        Mono.error(new IdentityException(ResponseMessage.LOGIN_FAILED));
        return null;
    }

}