package com.example.inventorygenius.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // Generate JWT from OAuth2 authentication
    public String generateToken(OAuth2AuthenticationToken auth) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(auth.getPrincipal().getAttribute("email"))
                .claim("name", auth.getPrincipal().getAttribute("name"))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    // Validate JWT and return claims
    public Claims validate(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
