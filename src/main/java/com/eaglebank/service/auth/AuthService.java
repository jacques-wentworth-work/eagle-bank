package com.eaglebank.service.auth;

import com.eaglebank.resource.JwtToken;

public interface AuthService {
    void create(String id, String password);
    void update(String id, String password);
    void delete(String id);
    JwtToken login(String authorizationHeader);
}
