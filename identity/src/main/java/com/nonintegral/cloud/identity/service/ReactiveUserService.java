package com.nonintegral.cloud.identity.service;

import com.nonintegral.cloud.identity.entity.User;
import com.nonintegral.cloud.identity.entity.response.ResponseMessage;
import reactor.core.publisher.Mono;

public interface ReactiveUserService {

    Mono<ResponseMessage> registerUser(User user);
    Mono<String> sendUserActivationLink(User u);
    Mono<ResponseMessage> readUserActivationToken(String token);
    Mono<User> activateUser(User user);
    Mono<User> credentialsEmailValid(User user);
    Mono<User> credentialsPasswordValid(User user);
    Mono<User> emailTaken(User user);
    Mono<User> createUser(User user);
    Mono<User> updateUserLastLogin(User user);
    Mono<ResponseMessage> changeUserPassword(User user, String newPassword);
    Mono<ResponseMessage> accountRecovery(String email);
    Mono<ResponseMessage> changePasswordByToken(String token, String newPassword);
    Mono<ResponseMessage> removeUser(String id);
    Mono<ResponseMessage> banUser(String id);

}
