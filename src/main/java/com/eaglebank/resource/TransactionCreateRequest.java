package com.eaglebank.resource;

import com.eaglebank.entity.TransactionType;

public record TransactionCreateRequest(
        Double amount,
        String currency,
        TransactionType type,
        String reference) {
}
