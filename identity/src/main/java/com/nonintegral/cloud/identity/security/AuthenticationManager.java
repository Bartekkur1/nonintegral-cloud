package com.nonintegral.cloud.identity.security;

import com.nonintegral.cloud.identity.entity.IdentityException;
import com.nonintegral.cloud.identity.entity.response.ResponseMessage;
import com.nonintegral.cloud.identity.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        try {
            String authToken = authentication.getCredentials().toString();
            Claims claims = jwtService.readToken(authToken);
            String username = claims.getSubject();

            if (username == null || !jwtService.validateToken(authToken))
                return Mono.error(new IdentityException(ResponseMessage.LOGIN_FAILED));

            List<String> rolesRaw = claims.get("role", List.class);
            List<SimpleGrantedAuthority> roles = rolesRaw.stream().map(role -> new SimpleGrantedAuthority(role.toString()))
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, roles);
            return Mono.just(auth);
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}
