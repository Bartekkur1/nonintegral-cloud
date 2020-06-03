package com.nonintegral.cloud.identity.service;

import com.nonintegral.cloud.identity.dao.UserRepository;
import com.nonintegral.cloud.identity.entity.IdentityException;
import com.nonintegral.cloud.identity.entity.User;
import com.nonintegral.cloud.identity.entity.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Component
public class UserServiceBase implements ReactiveUserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public Mono<ResponseMessage> registerUser(User user) {
        return Mono.just(user)
                .flatMap(this::emailTaken)
                .flatMap(this::credentialsEmailValid)
                .flatMap(this::credentialsPasswordValid)
                .flatMap(this::createUser)
                .flatMap(this::sendUserActivationLink)
                .flatMap(u -> Mono.just(ResponseMessage.ACCOUNT_CREATED))
                .onErrorResume(e -> Mono.just(Enum.valueOf(ResponseMessage.class, e.getMessage())));
    }

    public Mono<String> sendUserActivationLink(User u) {
        return Mono.just(UUID.randomUUID().toString())
                .flatMap(s -> {
                    u.setUtilToken(s);
                    return userRepository.save(u);
                })
                .map(k -> u.getUtilToken());
    }

    public Mono<ResponseMessage> readUserActivationToken(String token) {
        return userRepository.findByUtilToken(token)
                .switchIfEmpty(Mono.error(new IdentityException(ResponseMessage.INVALID_TOKEN)))
                .flatMap(this::activateUser)
                .flatMap(u -> Mono.just(ResponseMessage.ACCOUNT_ACTIVATED))
                .onErrorResume(e -> Mono.just(Enum.valueOf(ResponseMessage.class, e.getMessage())));
    }

    public Mono<User> activateUser(User u) {
        u.setActive(true);
        u.setUtilToken(null);
        return userRepository.save(u);
    }

    public Mono<User> credentialsEmailValid(User user) {
        if(user.getEmail().length() > 4)
            return Mono.just(user);
        else
            return Mono.error(new IdentityException(ResponseMessage.INVALID_EMAIL));
    }

    public Mono<User> credentialsPasswordValid(User user) {
        if(user.getPassword().length() > 4)
            return Mono.just(user);
        else {
            return Mono.error(new IdentityException(ResponseMessage.INVALID_PASSWORD));
        }
    }

    public Mono<User> emailTaken(User user) {
        return userRepository.existsByEmail(user.getEmail())
                .flatMap(b -> {
                    if(b) {
                        return Mono.error(new IdentityException(ResponseMessage.EMAIL_TAKEN));
                    } else {
                        return Mono.just(user);
                    }
                });
    }

    public Mono<User> createUser(User user) {
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        return userRepository.save(user)
                .switchIfEmpty(Mono.error(new IdentityException(ResponseMessage.ACCOUNT_CREATION_FAILED)));
    }

    public Mono<User> updateUserLastLogin(User user) {
        user.setLastLogin(new Date());
        return userRepository.save(user);
    }

    public Mono<ResponseMessage> changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user)
                .map(u -> ResponseMessage.PASSWORD_CHANGED);
    }

    public Mono<ResponseMessage> accountRecovery(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new IdentityException(ResponseMessage.RECOVERY_LINK_SENT)))
                .flatMap(u -> {
                    if(u.getActive()) {
                        u.setUtilToken(UUID.randomUUID().toString());
                        return userRepository.save(u);
                    }
                    return Mono.error(new IdentityException(ResponseMessage.BAD_REQUEST));
                })
                .map(u -> ResponseMessage.RECOVERY_LINK_SENT)
                .onErrorResume(e -> Mono.just(Enum.valueOf(ResponseMessage.class, e.getMessage())));
    }

    public Mono<ResponseMessage> changePasswordByToken(String token, String newPassword) {
        return userRepository.findByUtilToken(token)
                .switchIfEmpty(Mono.error(new IdentityException(ResponseMessage.INVALID_TOKEN)))
                .flatMap(u -> {
                    u.setUtilToken(null);
                    return userRepository.save(u);
                })
                .flatMap(u -> changeUserPassword(u, newPassword))
                .onErrorResume(e -> Mono.just(Enum.valueOf(ResponseMessage.class, e.getMessage())));
    }

    public Mono<ResponseMessage> removeUser(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new IdentityException(ResponseMessage.NOT_FOUND)))
                .flatMap(userRepository::delete)
                .map(r -> ResponseMessage.USER_REMOVED);
    }

    public Mono<ResponseMessage> banUser(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new IdentityException(ResponseMessage.NOT_FOUND)))
                .flatMap(u -> {
                    u.setBanned(true);
                    return userRepository.save(u);
                })
                .map(r -> ResponseMessage.USER_BANNED);
    }

}
