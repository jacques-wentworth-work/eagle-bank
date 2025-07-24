package com.eaglebank.service.account.impl;

import com.eaglebank.entity.Account;
import com.eaglebank.entity.User;
import com.eaglebank.exception.AccountNotFoundException;
import com.eaglebank.exception.UserNotFoundException;
import com.eaglebank.repository.BankAccountRepository;
import com.eaglebank.repository.UserRepository;
import com.eaglebank.service.account.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    private final BankAccountRepository accountRepo;
    private final UserRepository userRepo;

    public AccountServiceImpl(BankAccountRepository accountRepo, UserRepository userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    public Account create(Account account, String userId) {
        User user = userRepo
                .findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(UserNotFoundException.STANDARD_EXCEPTION_MESSAGE, userId)));

        String accNumber = "01" + new Random().nextInt(1_000_000);
        account.setAccountNumber(accNumber);
        account.setUser(user);
        account.setBalance(0.0);

        return accountRepo.save(account);
    }

    @Override
    public List<Account> get(String userId) {
        return accountRepo.findByUserId(userId);
    }

    @Override
    public Account get(String accountNumber, String userId) {
        return accountRepo
                .findByAccountNumberAndUserId(accountNumber, userId)
                .orElseThrow(() ->
                        new AccountNotFoundException(String.format(AccountNotFoundException.STANDARD_EXCEPTION_MESSAGE, accountNumber)));
    }

    @Override
    public Account update(String accountNumber, Account updated, String userId) {
        Account account = get(accountNumber, userId);

        if (updated.getName() != null && !updated.getName().isEmpty()) {
            account.setName(updated.getName());
        }
        if (updated.getAccountType() != null) {
            account.setAccountType(updated.getAccountType());
        }

        return accountRepo.save(account);
    }

    @Override
    public void delete(String accountNumber, String userId) {
        Account acc = get(accountNumber, userId);
        accountRepo.delete(acc);
    }
}

