package com.eaglebank.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserCreateRequest(
        @NotBlank String name,
        @Valid @NotNull AddressResource address,
        @NotBlank String phoneNumber,
        @Email @NotBlank String email) {
}
