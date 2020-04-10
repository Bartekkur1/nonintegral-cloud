package com.nonintegral.cloud.identity.security;

import com.nonintegral.cloud.identity.dao.UserRepository;
import com.nonintegral.cloud.identity.entity.IdentityException;
import com.nonintegral.cloud.identity.entity.User;
import com.nonintegral.cloud.identity.entity.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ReactiveAuthenticationFacadeBase implements ReactiveAuthenticationFacade {

    @Autowired
    private final UserRepository userRepository;

    public Mono<User> getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(email == null)
            return Mono.error(new IdentityException(ResponseMessage.UNAUTHORIZED));
        else
            return userRepository.findByEmail(email);
    }
}
