package com.eaglebank.resource;

import com.eaglebank.entity.TransactionType;

import java.time.LocalDateTime;

public record TransactionResponse(
        String id,
        Double amount,
        String currency,
        TransactionType type,
        String reference,
        String userId,
        LocalDateTime createdTimestamp) {
}

