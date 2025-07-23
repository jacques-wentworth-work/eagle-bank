package com.eaglebank.controller;

import com.eaglebank.entity.Role;
import com.eaglebank.exception.AccessForbiddenException;
import com.eaglebank.resource.UserCreateRequest;
import com.eaglebank.resource.UserResponse;
import com.eaglebank.resource.UserUpdateRequest;
import com.eaglebank.service.authentication.AuthenticationService;
import com.eaglebank.service.jwt.JwtService;
import com.eaglebank.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request) {
        UserResponse userResponse = userService.create(request);
        authenticationService.create(userResponse.id(), request.password(), Role.CUSTOMER);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @PathVariable String userId) {

        validateUser(userId, jwt);

        return ok(userService.get(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @PathVariable String userId,
            @Valid @RequestBody UserUpdateRequest request) {
        validateUser(userId, jwt);

        UserResponse updateUser = userService.update(userId, request);
        if (request.password() != null && !request.password().isEmpty()) {
            authenticationService.update(userId, request.password());
        }

        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @PathVariable String userId) {
        validateUser(userId, jwt);

        userService.delete(userId);
        authenticationService.delete(userId);

        return ResponseEntity.noContent().build();
    }

    private void validateUser(String userId, String jwt) {
        String tokenUserId = jwtService.extractUsername(jwt.substring("Bearer ".length()));
        if (!tokenUserId.equals(userId)) {
            throw new AccessForbiddenException("You are not authorized to access this user's information");
        }
    }
}
