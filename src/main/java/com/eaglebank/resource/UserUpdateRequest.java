package com.eaglebank.resource;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserUpdateRequest(
        String name,
        AddressResource address,
        @Pattern(regexp = "^\\+[1-9]\\d{1,14}$") String phoneNumber,
        @Email String email,
        @Size(min = 8, max = 64) String password) {
}
