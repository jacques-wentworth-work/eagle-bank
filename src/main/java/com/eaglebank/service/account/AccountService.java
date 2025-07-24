package com.eaglebank.service.account;

import com.eaglebank.entity.Account;

import java.util.List;

public interface AccountService {
    Account create(Account account, String userId);
    List<Account> get(String userId);
    Account get(String accountNumber, String userId);
    Account update(String accountNumber, Account account, String userId);
    void delete(String accountNumber, String userId);
}

