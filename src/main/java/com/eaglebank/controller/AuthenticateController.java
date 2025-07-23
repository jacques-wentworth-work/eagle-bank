package com.eaglebank.controller;

import com.eaglebank.resource.JwtToken;
import com.eaglebank.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/authenticate")
@RequiredArgsConstructor
public class AuthenticateController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<JwtToken> createToken(HttpServletRequest request) {
        JwtToken token = authenticationService.login(
                request.getHeader(HttpHeaders.AUTHORIZATION));

        return ok(token);
    }
}
