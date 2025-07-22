package com.eaglebank.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UserCreateRequest(
        @NotBlank String name,
        @Valid @NotNull AddressResource address,
        @NotBlank @Pattern(regexp = "^\\+[1-9]\\d{1,14}$") String phoneNumber,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, max = 64) String password) {
}
