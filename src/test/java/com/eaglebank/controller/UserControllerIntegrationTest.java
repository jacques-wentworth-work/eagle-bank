package com.eaglebank.controller;

import com.eaglebank.resource.AddressResource;
import com.eaglebank.resource.UserCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_success() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Alice Smith",
                new AddressResource("Line 1", "Line 2", "", "City", "County", "ZIP123"),
                "+441234567890",
                "alice@example.com"
        );

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice Smith"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void create_failWitValidationErrorNameNull() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                null,
                AddressResource.builder().line1("L1").town("T1").county("C1").postcode("P1").build(),
                "0", "jack@example.com");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.details[0].field").value("name"))
                .andExpect(jsonPath("$.details[0].message").value("must not be blank"))
                .andExpect(jsonPath("$.details[0].type").value("NotBlank"));
    }

    @Test
    void create_failWitValidationErrorNameBlank() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "",
                AddressResource.builder().line1("L1").town("T1").county("C1").postcode("P1").build(),
                "0", "jack@example.com");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.details[0].field").value("name"))
                .andExpect(jsonPath("$.details[0].message").value("must not be blank"))
                .andExpect(jsonPath("$.details[0].type").value("NotBlank"));
    }

    @Test
    void create_failWitValidationErrorAddressNull() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Bob Builder", null, "0", "jack@example.com");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.details[0].field").value("address"))
                .andExpect(jsonPath("$.details[0].message").value("must not be null"))
                .andExpect(jsonPath("$.details[0].type").value("NotNull"));
    }

    @Test
    void create_failWitValidationErrorAddressLine1Null() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Bob Builder",
                AddressResource.builder().town("T1").county("C1").postcode("P1").build(),
                "0", "jack@example.com");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.details[0].field").value("address.line1"))
                .andExpect(jsonPath("$.details[0].message").value("must not be blank"))
                .andExpect(jsonPath("$.details[0].type").value("NotBlank"));
    }

    @Test
    void create_failWitValidationErrorAddressTownNull() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Bob Builder",
                AddressResource.builder().line1("L1").county("C1").postcode("P1").build(),
                "0", "jack@example.com");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.details[0].field").value("address.town"))
                .andExpect(jsonPath("$.details[0].message").value("must not be blank"))
                .andExpect(jsonPath("$.details[0].type").value("NotBlank"));
    }

    @Test
    void create_failWitValidationErrorAddressCountyNull() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Bob Builder",
                AddressResource.builder().line1("L1").town("T1").postcode("P1").build(),
                "0", "jack@example.com");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.details[0].field").value("address.county"))
                .andExpect(jsonPath("$.details[0].message").value("must not be blank"))
                .andExpect(jsonPath("$.details[0].type").value("NotBlank"));
    }

    @Test
    void create_failWitValidationErrorAddressPostCodeNull() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Bob Builder",
                AddressResource.builder().line1("L1").town("T1").county("C1").build(),
                "0", "jack@example.com");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.details[0].field").value("address.postcode"))
                .andExpect(jsonPath("$.details[0].message").value("must not be blank"))
                .andExpect(jsonPath("$.details[0].type").value("NotBlank"));
    }

    @Test
    void get() {
        //TODO: get user endpoint tests
        assertTrue(true);
    }

    @Test
    void updateUserById() {
        //TODO: update user endpoint tests
        assertTrue(true);
    }

    @Test
    void deleteUser() {
        //TODO: delete user endpoint tests
        assertTrue(true);
    }
}
