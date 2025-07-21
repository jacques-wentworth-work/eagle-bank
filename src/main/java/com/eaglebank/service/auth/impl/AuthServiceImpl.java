package com.eaglebank.service.auth.impl;

import com.eaglebank.exception.UserLoginException;
import com.eaglebank.resource.JwtToken;
import com.eaglebank.service.auth.AuthService;
import com.eaglebank.service.auth.component.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    public static final String INVALID_AUTHENTICATION_HEADER = "Invalid authentication header";
    public static final String BASIC_AUTH_PREFIX = "Basic";
    private final JwtUtil jwtUtil;

    @Override
    public JwtToken login(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BASIC_AUTH_PREFIX)) {
            throw new UserLoginException(INVALID_AUTHENTICATION_HEADER);
        }

        return buildJwtToken(authorizationHeader);
    }

    private JwtToken buildJwtToken(String authorizationHeader) {
        // credentials = username:password
        String credentials = decodeHeader(authorizationHeader);

        final String[] values = credentials.split(":", 2);
        String username = values[0];
        String password = values[1];

        if (isValidUser(username, password)) {
            return new JwtToken(jwtUtil.generateToken(username));
        } else {
            throw new UserLoginException("Invalid username or password");
        }
    }

    private static String decodeHeader(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic " .length());
        return new String(Base64.getDecoder().decode(base64Credentials));
    }

    private boolean isValidUser(String username, String password) {
        //TODO: implement password check
        return true;
    }

}
