package com.eaglebank.service.jwt.impl;

import com.eaglebank.exception.UserLoginException;
import com.eaglebank.service.jwt.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey;
    private final int jwtTTL;

    public JwtServiceImpl(@Value("${jwt.secrets}") String jwtSecret,
                          @Value("${jwt.ttl-minutes}") int jwtTtlMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        this.jwtTTL = jwtTtlMinutes * 60 * 1000;
    }

    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtTTL))
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
