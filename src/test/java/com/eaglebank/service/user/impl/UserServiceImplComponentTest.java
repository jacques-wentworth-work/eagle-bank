package com.eaglebank.service.user.impl;

import com.eaglebank.repository.UserRepository;
import com.eaglebank.resource.AddressResource;
import com.eaglebank.resource.UserCreateRequest;
import com.eaglebank.resource.UserResponse;
import com.eaglebank.resource.UserUpdateRequest;
import com.eaglebank.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplComponentTest {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveUser() {
        // Create user
        UserCreateRequest request = new UserCreateRequest(
                "Test User",
                new AddressResource("Line 1", "Line 2", "", "Town", "County", "AB12 3CD"),
                "+441234567890",
                "test@example.com"
        );

        UserResponse created = service.create(request);

        assertNotNull(created.id());
        assertEquals("Test User", created.name());

        // Retrieve user
        UserResponse fetched = service.get(created.id());
        assertEquals(created.id(), fetched.id());
        assertEquals("test@example.com", fetched.email());
    }

    @Test
    void shouldCreateAndUpdateUser() {
        // Create user
        UserCreateRequest createRequest = new UserCreateRequest(
                "Update Test",
                new AddressResource("Line 1", "", "", "Town", "County", "PC123"),
                "+441234567890",
                "update@example.com"
        );

        UserResponse created = service.create(createRequest);
        assertNotNull(created.id());
        assertEquals("Update Test", created.name());
        assertEquals("update@example.com", created.email());

        // Update user
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "Updated Name",
                new AddressResource("New Line", "Apt 2", "", "New Town", "New County", "NEW123"),
                "+440000000000",
                "updated@example.com"
        );

        UserResponse updated = service.update(created.id(), updateRequest);

        assertEquals("Updated Name", updated.name());
        assertEquals("updated@example.com", updated.email());
        assertEquals("New Town", updated.address().town());
        assertEquals("+440000000000", updated.phoneNumber());
    }


    @Test
    void shouldCreateAndDeleteUser() {
        // Create user
        UserCreateRequest request = new UserCreateRequest(
                "Delete Me",
                new AddressResource("L1", "", "", "Town", "County", "PC"),
                "+441111111111",
                "delete@example.com"
        );

        UserResponse created = service.create(request);
        assertDoesNotThrow(() -> service.get(created.id()));

        // Delete user
        service.delete(created.id());
        assertTrue(userRepository.findById(created.id()).isEmpty());
    }
}
