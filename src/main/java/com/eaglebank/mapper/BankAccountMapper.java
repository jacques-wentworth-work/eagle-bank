package com.eaglebank.mapper;

import com.eaglebank.entity.Account;
import com.eaglebank.resource.BankAccountCreateRequest;
import com.eaglebank.resource.BankAccountResponse;
import com.eaglebank.resource.BankAccountUpdateRequest;

import java.util.List;

public class BankAccountMapper {

    public static Account toEntity(BankAccountCreateRequest dto) {
        Account acc = new Account();
        acc.setName(dto.name());
        acc.setAccountType(dto.accountType());
        return acc;
    }

    public static Account toEntity(BankAccountUpdateRequest dto) {
        Account acc = new Account();
        acc.setName(dto.name());
        acc.setAccountType(dto.accountType());
        return acc;
    }

    public static BankAccountResponse toResponse(Account acc) {
        return new BankAccountResponse(
                acc.getAccountNumber(),
                acc.getSortCode(),
                acc.getName(),
                acc.getAccountType(),
                acc.getBalance(),
                acc.getCurrency(),
                acc.getCreatedTimestamp(),
                acc.getUpdatedTimestamp()
        );
    }

    public static List<BankAccountResponse> toResponseList(List<Account> accounts) {
        return accounts.stream()
                .map(BankAccountMapper::toResponse)
                .toList();
    }
}

