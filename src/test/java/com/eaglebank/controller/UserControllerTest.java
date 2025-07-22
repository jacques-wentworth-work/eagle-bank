package com.eaglebank.controller;

import com.eaglebank.resource.AddressResource;
import com.eaglebank.resource.UserCreateRequest;
import com.eaglebank.resource.UserResponse;
import com.eaglebank.resource.UserUpdateRequest;
import com.eaglebank.service.auth.AuthService;
import com.eaglebank.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_success() {
        UserCreateRequest request = new UserCreateRequest("Name One",
                new AddressResource("L1", "", "", "T1", "C1" ,"P1"),
                "0123456789", "email@example.com", "password");
        UserResponse userResponse = new UserResponse("1", "Name One",
                new AddressResource("L1", "", "", "T1", "C1" ,"P1"),
                "0123456789", "email@example.com");

        when(userService.create(any(UserCreateRequest.class))).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());

        // Verify that authService.create was called
        verify(authService).create(userResponse.id(), request.password());
    }

    @Test
    void get_success() {
        String userId = "1";
        UserResponse userResponse = new UserResponse("1", "Name One",
                new AddressResource("L1", "", "", "T1", "C1" ,"P1"),
                "0123456789", "email@example.com");


        when(userService.get(userId)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    void update_success() {
        String userId = "1";
        UserUpdateRequest updateRequest = new UserUpdateRequest("Name One",
                new AddressResource("L1", "", "", "T1", "C1" ,"P1"),
                "0123456789", "email@example.com", "");
        UserResponse updatedUserResponse = new UserResponse("1", "Name One",
                new AddressResource("L1", "", "", "T1", "C1" ,"P1"),
                "0123456789", "email@example.com");


        when(userService.update(userId, updateRequest)).thenReturn(updatedUserResponse);

        ResponseEntity<UserResponse> response = userController.updateUserById(userId, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUserResponse, response.getBody());
    }

    @Test
    void delete_success() {
        String userId = "1";

        doNothing().when(userService).delete(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).delete(userId);
    }
}
