package com.eaglebank.service.user.impl;

import com.eaglebank.entity.Address;
import com.eaglebank.entity.User;
import com.eaglebank.exception.DuplicateEmailException;
import com.eaglebank.exception.UserNotFoundException;
import com.eaglebank.repository.UserRepository;
import com.eaglebank.resource.AddressResource;
import com.eaglebank.resource.UserCreateRequest;
import com.eaglebank.resource.UserResponse;
import com.eaglebank.resource.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_success() {
        String line1 = "Line1";
        String town = "Town1";
        String county = "County1";
        String postcode = "Postcode1";
        AddressResource address = new AddressResource(line1, null, null, town, county, postcode);

        String name = "Jane Doe";
        String phoneNumber = "+441234567890";
        String email = "jane@example.com";
        String password = "secure_password123";
        UserCreateRequest request = new UserCreateRequest(name, address, phoneNumber, email, password);

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());
        when(userRepository.findTopByIdStartingWithOrderByIdDesc(anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        UserResponse response = userService.create(request);

        assertNotNull(response);
        assertEquals("usr-jandoe1", response.id());
        assertEquals(name, response.name());
        assertEquals(phoneNumber, response.phoneNumber());
        assertEquals(email, response.email());

        assertNotNull(response.address());
        assertEquals(line1, response.address().line1());
        assertNull(response.address().line2());
        assertNull(response.address().line3());
        assertEquals(postcode, response.address().postcode());
        assertEquals(town, response.address().town());
        assertEquals(county, response.address().county());
        assertEquals(postcode, response.address().postcode());
    }

    @Test
    void create_shouldThrowDuplicateEmailException() {
        UserCreateRequest request = UserCreateRequest.builder().build();

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEmailException.class, () -> userService.create(request));
    }

    @Test
    void get_success() {
        String line1 = "Line1";
        String town = "Town1";
        String county = "County1";
        String postcode = "Postcode1";
        Address address = Address.builder()
                .line1(line1)
                .town(town)
                .county(county)
                .postcode(postcode)
                .build();

        String id = "usr-jandoe1";
        String name = "Jane Doe";
        String phoneNumber = "+441234567890";
        String email = "jane@example.com";
        User user = User.builder()
                .id(id)
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();

        when(userRepository.findById("usr-janedoe")).thenReturn(Optional.of(user));

        UserResponse response = userService.get("usr-janedoe");

        assertEquals(name, response.name());
    }

    @Test
    void get_shouldThrowUserNotFoundException() {
        String id = "non-existent";

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.get(id));
    }

    @Test
    void update_success() {
        String userId = "usr-jandoe1";
        User existing = User.builder()
                .id(userId)
                .name("Jane Doe")
                .email("jane@example.com")
                .phoneNumber("+441234567890")
                .address(Address.builder().line1("Old Line").town("Old Town").county("Old County").postcode("12345").build())
                .build();

        UserUpdateRequest request = new UserUpdateRequest(
                "Jane Dover",
                new AddressResource("New Line", "", "", "New Town", "New County", "54321"),
                "+441112223334",
                "jane.dover@example.com",
                "");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(userRepository.findByEmail("jane.dover@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        UserResponse response = userService.update(userId, request);

        assertNotNull(response);
        assertEquals("Jane Dover", response.name());
        assertEquals("jane.dover@example.com", response.email());
        assertEquals("+441112223334", response.phoneNumber());

        assertNotNull(response.address());
        assertEquals("New Line", response.address().line1());
        assertEquals("New Town", response.address().town());
        assertEquals("New County", response.address().county());
        assertEquals("54321", response.address().postcode());

    }

    @Test
    void update_shouldThrowUserNotFoundException() {
        String userId = "missing-id";
        UserUpdateRequest request = UserUpdateRequest.builder().build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(userId, request));
    }

    @Test
    void update_shouldThrowDuplicateEmailException() {
        String userId = "usr-jandoe1";

        User existing = User.builder()
                .id(userId)
                .email("jane@example.com")
                .build();

        UserUpdateRequest request = UserUpdateRequest.builder()
                .email("existing@example.com")
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existing));
        when(userRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEmailException.class, () -> userService.update(userId, request));
    }

    @Test
    void delete_success() {
        String userId = "usr-johsmi1";
        User existing = User.builder()
                .id(userId)
                .email("jane@example.com")
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existing));
        doNothing()
                .when(userRepository)
                .delete(existing);

        assertDoesNotThrow(() -> userService.delete(userId));
        verify(userRepository).delete(existing);
    }

    @Test
    void delete_shouldThrowUserNotFoundException() {
        String userId = "non-existent-id";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        verify(userRepository, never()).deleteById(any());
    }
}

