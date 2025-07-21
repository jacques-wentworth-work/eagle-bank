package com.eaglebank.resource;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String name,
        AddressResource address,
        String phoneNumber,
        String email) {
}
