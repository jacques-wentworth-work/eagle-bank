package com.eaglebank.controller;

import com.eaglebank.resource.UserCreateRequest;
import com.eaglebank.resource.UserResponse;
import com.eaglebank.resource.UserUpdateRequest;
import com.eaglebank.service.auth.AuthService;
import com.eaglebank.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request) {
        UserResponse userResponse = userService.create(request);
        authService.create(userResponse.id(), request.password());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable String userId) {
        return ok(userService.get(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserById(
            @PathVariable String userId,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse updateUser = userService.update(userId, request);
        if (request.password() != null && !request.password().isEmpty()) {
            authService.update(userId, request.password());
        }

        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(
            @PathVariable String userId) {
        userService.delete(userId);
        authService.delete(userId);

        return ResponseEntity.noContent().build();
    }
}
