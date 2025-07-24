package com.eaglebank.resource;

import com.eaglebank.entity.AccountType;

import java.time.LocalDateTime;

public record BankAccountResponse(
        String accountNumber,
        String sortCode,
        String name,
        AccountType accountType,
        Double balance,
        String currency,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp) {
}
