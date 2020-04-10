package com.nonintegral.cloud.identity.service;

import com.nonintegral.cloud.identity.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtServiceBase implements JwtService {

    @Value("${jwt.secret}")
    public String jwtSecret;

    public String generateToken(User user) {
        long now = System.currentTimeMillis();

        final Date createdAt = new Date(now);
        final Date expiredAt = new Date(now + 150000);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRoles())
                .setIssuedAt(createdAt)
                .setExpiration(expiredAt)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(jwtSecret.getBytes()))
                .compact();
    }

    public Claims readToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(jwtSecret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token) {
        return !readToken(token).getExpiration().before(new Date());
    }

}
