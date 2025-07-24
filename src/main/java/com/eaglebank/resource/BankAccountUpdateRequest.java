package com.eaglebank.resource;

import com.eaglebank.entity.AccountType;

public record BankAccountUpdateRequest(
        String name,
        AccountType accountType) {
}
