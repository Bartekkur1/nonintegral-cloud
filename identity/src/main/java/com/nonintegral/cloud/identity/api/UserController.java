package com.nonintegral.cloud.identity.api;

import com.nonintegral.cloud.identity.entity.User;
import com.nonintegral.cloud.identity.entity.request.*;
import com.nonintegral.cloud.identity.entity.response.IdentityResponseBody;
import com.nonintegral.cloud.identity.security.ReactiveAuthenticationFacade;
import com.nonintegral.cloud.identity.service.ReactiveUserService;
import com.nonintegral.cloud.identity.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private final ReactiveUserService userService;

    @Autowired
    private final ReactiveAuthenticationFacade authenticationFacade;

    @PostMapping("/register")
    public Mono<ResponseEntity<IdentityResponseBody>> register(@RequestBody RegisterRequest request) {
        return userService.registerUser(new User(request))
                .flatMap(ResponseUtil::transformResponseMessage);
    }

    @PostMapping("/activate")
    public Mono<ResponseEntity<IdentityResponseBody>> activateAccount(@RequestBody TokenRequest request) {
        return userService.readUserActivationToken(request.getToken())
                .flatMap(ResponseUtil::transformResponseMessage);
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Mono<ResponseEntity<IdentityResponseBody>> changePassword(@RequestBody ChangePasswordRequest request) {
        return authenticationFacade.getUser()
                .flatMap(u -> userService.changeUserPassword(u, request.getNewPassword()))
                .flatMap(ResponseUtil::transformResponseMessage);
    }

    @PostMapping("/accountRecovery")
    public Mono<ResponseEntity<IdentityResponseBody>> accountRecovery(@RequestBody AccountRecoveryRequest request) {
        return userService.accountRecovery(request.getEmail())
                .flatMap(ResponseUtil::transformResponseMessage);
    }

    @PostMapping("/passwordRecovery")
    public Mono<ResponseEntity<IdentityResponseBody>> accountRecover(@RequestBody AccountRecoverRequest request) {
        return userService.changePasswordByToken(request.getToken(), request.getNewPassword())
                .flatMap(ResponseUtil::transformResponseMessage);
    }
}
