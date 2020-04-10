package com.nonintegral.cloud.identity.api;

import com.nonintegral.cloud.identity.entity.request.LoginRequest;
import com.nonintegral.cloud.identity.entity.response.IdentityResponseBody;
import com.nonintegral.cloud.identity.entity.response.LoginResponse;
import com.nonintegral.cloud.identity.entity.response.ResponseMessage;
import com.nonintegral.cloud.identity.service.ReactiveAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AuthController {

    @Autowired
    private final ReactiveAuthService reactiveAuthServiceBase;

    @PostMapping("/login")
    public Mono<ResponseEntity> login(@RequestBody LoginRequest request) {
        return reactiveAuthServiceBase.login(request)
                .map(s -> new IdentityResponseBody(ResponseMessage.LOGGED_IN, new LoginResponse(s)))
                .onErrorResume(e -> Mono.just(new IdentityResponseBody(ResponseMessage.LOGIN_FAILED, e.getMessage())))
                .map(irb -> new ResponseEntity<>(irb, irb.getHttpStatus()));
    }

}
