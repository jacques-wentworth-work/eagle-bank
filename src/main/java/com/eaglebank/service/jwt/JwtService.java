package com.eaglebank.service.jwt;

import java.util.Map;

public interface JwtService {
    String generateToken(String username, Map<String, Object> extraClaims);
    String extractUsername(String token);
}
