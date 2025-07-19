package com.akshay.StoreMaster.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {

//    @Value("${jwt.secret}")
    private String secretKey;

    public String generateJwtToken(String email, String role){
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Instant now = Instant.now();
        Date issueDate = Date.from(now);
        Date expiration = Date.from(now.plus(1, ChronoUnit.HOURS));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issueDate)
                .setExpiration(expiration)
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}