package com.eaglebank.integration;

import com.eaglebank.entity.Address;
import com.eaglebank.entity.Role;
import com.eaglebank.entity.User;
import com.eaglebank.entity.UserAuthentication;
import com.eaglebank.repository.UserAuthenticationRepository;
import com.eaglebank.repository.UserRepository;
import com.eaglebank.resource.AddressResource;
import com.eaglebank.resource.UserCreateRequest;
import com.eaglebank.service.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userAuthenticationRepository.deleteAll();
    }

    @Test
    void create_success() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Alice Smith",
                new AddressResource("Line 1", "Line 2", "", "City", "County", "ZIP123"),
                "+441234567890",
                "alice@example.com",
                "secure_password123");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("usr-alismi1"))
                .andExpect(jsonPath("$.name").value("Alice Smith"))
                .andExpect(jsonPath("$.address.line1").value("Line 1"))
                .andExpect(jsonPath("$.phoneNumber").value("+441234567890"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));

        Optional<UserAuthentication> optionalUserAuth = userAuthenticationRepository.findByUsername("usr-alismi1");
        assertTrue(optionalUserAuth.isPresent());
        UserAuthentication userAuthentication = optionalUserAuth.get();
        assertEquals("usr-alismi1", userAuthentication.getUsername());
        assertTrue(passwordEncoder.matches("secure_password123", userAuthentication.getPassword()));
        assertEquals(Role.CUSTOMER, userAuthentication.getRole());
    }

    @Test
    void create_failWitValidationErrorNameNull() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                null,
                AddressResource.builder().line1("L1").town("T1").county("C1").postcode("P1").build(),
                "+1234567890", "jack@example.com", "secure_password123");

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
                "+1234567890", "jack@example.com", "secure_password123");

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
                "Bob Builder",
                null,
                "+1234567890", "jack@example.com", "secure_password123");

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
                "+1234567890", "jack@example.com", "secure_password123");

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
                "+1234567890", "jack@example.com", "secure_password123");

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
                "+1234567890", "jack@example.com", "secure_password123");

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
                "+1234567890", "jack@example.com", "secure_password123");

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
    void create_failWitValidationErrorPasswordBlank() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Bob Builder",
                AddressResource.builder().line1("L1").postcode("P1").town("T1").county("C1").build(),
                "+1234567890", "jack@example.com", "");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(equalTo(2))))
                .andExpect(jsonPath("$.details[*].field", everyItem(is("password"))))
                .andExpect(jsonPath("$.details[*].message", containsInAnyOrder("size must be between 8 and 64", "must not be blank")))
                .andExpect(jsonPath("$.details[*].type", containsInAnyOrder("Size", "NotBlank")));
    }

    @Test
    void create_failWitValidationErrorPasswordWrongSize() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Bob Builder",
                AddressResource.builder().line1("L1").postcode("P1").town("T1").county("C1").build(),
                "+1234567890", "jack@example.com", "123456");

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.details[0].field").value("password"))
                .andExpect(jsonPath("$.details[0].message").value("size must be between 8 and 64"))
                .andExpect(jsonPath("$.details[0].type").value("Size"));
    }

    @Test
    void get_success() throws Exception {
        String userId = "usr-samsmi1";

        User saved = aUserAndAuth(userId);
        String token = createSampleJwtToken(userId, generateExtraClaims(userId, "CUSTOMER"));

        mockMvc.perform(get("/v1/users/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Samuel Smith"))
                .andExpect(jsonPath("$.email").value("sam.smith@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.address.line1").value("L1"))
                .andExpect(jsonPath("$.address.postcode").value("P1"))
                .andExpect(jsonPath("$.address.town").value("T1"))
                .andExpect(jsonPath("$.address.county").value("C1"));
    }

    @Test
    void get_notFound() throws Exception {
        String userId = "usr-samsmi1";

        anAuthUser(userId);
        String token = createSampleJwtToken(userId, generateExtraClaims(userId, "CUSTOMER"));

        mockMvc.perform(get("/v1/users/{id}", "usr-samsmi1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }


    @Test
    void get_forbidden() throws Exception {
        String userId = "usr-samsmi1";

        aUserAndAuth(userId);
        String token = createSampleJwtToken(userId, generateExtraClaims(userId, "CUSTOMER"));

        mockMvc.perform(get("/v1/users/usr-not-found")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() {
        //TODO: update user endpoint tests
        assertTrue(true);
    }

    @Test
    void delete() {
        //TODO: delete user endpoint tests
        assertTrue(true);
    }

    private User aUserAndAuth(String userId) {
        User saved = aUser(userId);
        anAuthUser(userId);

        return saved;
    }

    private User aUser(String userId) {
        return userRepository.save(User.builder()
                .id(userId)
                .name("Samuel Smith")
                .email("sam.smith@example.com")
                .phoneNumber("1234567890")
                .address(Address.builder()
                        .line1("L1").postcode("P1").town("T1").county("C1")
                        .build())
                .build());
    }

    private void anAuthUser(String userId) {
        userAuthenticationRepository.save(UserAuthentication.builder()
                .username(userId)
                .password(passwordEncoder.encode("password123"))
                .role(Role.CUSTOMER)
                .build());
    }

    private String createSampleJwtToken(String userId, Map<String, Object> claims) {
        return jwtService.generateToken(userId, claims);
    }

    private Map<String, Object> generateExtraClaims(String userId, String role) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", userId);
        extraClaims.put("role", role);
        return extraClaims;
    }
}
