package com.nonintegral.cloud.identity.service;

import com.nonintegral.cloud.identity.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateToken(User user);
    Claims readToken(String token);
    Boolean validateToken(String token);

}
