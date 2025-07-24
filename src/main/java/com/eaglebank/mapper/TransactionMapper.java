package com.eaglebank.mapper;

import com.eaglebank.entity.Transaction;
import com.eaglebank.resource.TransactionCreateRequest;
import com.eaglebank.resource.TransactionResponse;

import java.util.List;

public class TransactionMapper {

    public static Transaction toEntity(TransactionCreateRequest dto) {
        Transaction txn = new Transaction();
        txn.setAmount(dto.amount());
        txn.setCurrency(dto.currency());
        txn.setType(dto.type());
        txn.setReference(dto.reference());
        return txn;
    }

    public static TransactionResponse toResponse(Transaction txn) {
        return new TransactionResponse(
                txn.getId(),
                txn.getAmount(),
                txn.getCurrency(),
                txn.getType(),
                txn.getReference(),
                "123", //txn.getUser().getId(),
                txn.getCreatedTimestamp()
        );
    }

    public static List<TransactionResponse> toResponseList(List<Transaction> txns) {
        return txns.stream()
                .map(TransactionMapper::toResponse)
                .toList();
    }
}

