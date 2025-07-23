package com.eaglebank.service.authentication.impl;

import com.eaglebank.entity.Role;
import com.eaglebank.entity.UserAuthentication;
import com.eaglebank.exception.UserAuthNotFoundException;
import com.eaglebank.exception.UserLoginException;
import com.eaglebank.repository.UserAuthenticationRepository;
import com.eaglebank.resource.JwtToken;
import com.eaglebank.service.authentication.AuthenticationService;
import com.eaglebank.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    public static final String INVALID_AUTHENTICATION_HEADER = "Invalid authentication header";
    public static final String INVALID_USER_ID = "Invalid user id";

    public static final String BASIC_AUTH_PREFIX = "Basic";

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthenticationRepository repository;
    private final JwtService jwtService;
    private final UserAuthenticationRepository userAuthenticationRepository;


    @Override
    public void create(String username, String password, Role role) {
        repository.save(UserAuthentication.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build());
    }

    @Override
    public void update(String username, String password) {
        UserAuthentication userAuth = repository.findByUsername(username)
                .orElseThrow(() -> new UserAuthNotFoundException("User authentication not found"));

        userAuth.setPassword(passwordEncoder.encode(password));
        repository.save(userAuth);
    }

    @Override
    public void delete(String username) {
        repository.delete(repository.findByUsername(username)
                .orElseThrow(() -> new UserAuthNotFoundException("User authentication not found")));
    }

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

        Authentication authenticate;
        try {
            authenticate = authenticate(username, password);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            throw new UserLoginException(e.getMessage());
        }

        if (authenticate != null && authenticate.isAuthenticated()) {
            UserAuthentication userAuthentication =
                    userAuthenticationRepository.findByUsername(username)
                            .orElse(new UserAuthentication());

            return new JwtToken(jwtService.generateToken(username,
                    generateExtraClaims(userAuthentication)));
        } else {
            throw new UserLoginException("Invalid username or password");
        }
    }

    private static String decodeHeader(String authorizationHeader) {
        try {
            String base64Credentials = authorizationHeader.substring("Basic ".length());
            return new String(Base64.getDecoder().decode(base64Credentials));
        } catch (IllegalArgumentException e) {
            throw new UserLoginException(INVALID_AUTHENTICATION_HEADER);
        }
    }

    private Authentication authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    private Map<String, Object> generateExtraClaims(UserAuthentication userAuthentication) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", userAuthentication.getUsername());
        extraClaims.put("role", userAuthentication.getRole());
        return extraClaims;
    }
}
