package com.nonintegral.cloud.identity.api;

import com.nonintegral.cloud.identity.dao.UserRepository;
import com.nonintegral.cloud.identity.dto.UserDto;
import com.nonintegral.cloud.identity.entity.response.IdentityResponseBody;
import com.nonintegral.cloud.identity.service.ReactiveUserService;
import com.nonintegral.cloud.identity.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping( path = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ReactiveUserService userService;

    // TODO: Move method params to request class
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Flux<UserDto> getUsers(@RequestParam long page, @RequestParam long max) {
        return userRepository.findAll()
                .skip((page - 1) * max)
                .take(max)
                .map(UserDto::new);
    }

    @DeleteMapping("/user/{id}")
    public Mono<ResponseEntity<IdentityResponseBody>> removeUser(@RequestParam String id) {
        return userService.removeUser(id)
                .flatMap(ResponseUtil::transformResponseMessage);
    }

    @PostMapping("/user/{id}/ban")
    public Mono<ResponseEntity<IdentityResponseBody>> banUser(@RequestParam String id) {
        return userService.banUser(id)
                .flatMap(ResponseUtil::transformResponseMessage);
    }

}
