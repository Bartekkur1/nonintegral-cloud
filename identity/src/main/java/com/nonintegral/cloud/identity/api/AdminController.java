package com.nonintegral.cloud.identity.api;

import com.nonintegral.cloud.identity.dao.UserRepository;
import com.nonintegral.cloud.identity.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
@RequestMapping( path = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    @Autowired
    private final UserRepository userRepository;

    // TODO: Move method params to request class
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Flux<UserDto> getUsers(@RequestParam long page, @RequestParam long max) {
        // TODO: Move pagination to somewhere
        return userRepository.findAll()
                .skip((page - 1) * max)
                .take(max)
                .map(UserDto::new);
    }

    // TODO: Implement remaining admin methods

}
