package com.eaglebank.service.auth;

import com.eaglebank.resource.JwtToken;

public interface AuthService {
    JwtToken login(String authorizationHeader);
}
