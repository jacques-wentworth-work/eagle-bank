package com.eaglebank.resource;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UserUpdateRequest(
        String name,
        AddressResource address,
        String phoneNumber,
        @Email String email
) {
}
