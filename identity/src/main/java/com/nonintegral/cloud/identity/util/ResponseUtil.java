package com.nonintegral.cloud.identity.util;

import com.nonintegral.cloud.identity.entity.response.IdentityResponseBody;
import com.nonintegral.cloud.identity.entity.response.ResponseMessage;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public class ResponseUtil {

    public static Mono<ResponseEntity<IdentityResponseBody>> transformResponseMessage(ResponseMessage response) {
        return Mono.just(response)
                .onErrorResume(e -> Mono.just(Enum.valueOf(ResponseMessage.class, e.toString())))
                .map(e -> new IdentityResponseBody(e, response.getMessage()))
                .map(irb -> new ResponseEntity<>(irb, irb.getHttpStatus()));
    }

}
