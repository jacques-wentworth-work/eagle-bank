package com.eaglebank.service.authentication;

import com.eaglebank.entity.Role;
import com.eaglebank.resource.JwtToken;

public interface AuthenticationService {
    void create(String username, String password, Role role);
    void update(String username, String password);
    void delete(String username);
    JwtToken login(String authorizationHeader);
}
