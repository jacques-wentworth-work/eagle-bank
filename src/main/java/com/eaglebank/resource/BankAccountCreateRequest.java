package com.eaglebank.resource;

import com.eaglebank.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BankAccountCreateRequest(
        @NotBlank String name,
        @NotNull AccountType accountType) {
}
