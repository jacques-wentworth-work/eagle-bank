package com.eaglebank.service.transaction.impl;

import com.eaglebank.entity.Account;
import com.eaglebank.entity.Transaction;
import com.eaglebank.entity.TransactionType;
import com.eaglebank.exception.AccountNotFoundException;
import com.eaglebank.exception.TransactionNotFoundException;
import com.eaglebank.exception.UnprocessedTransactionException;
import com.eaglebank.repository.BankAccountRepository;
import com.eaglebank.repository.TransactionRepository;
import com.eaglebank.service.transaction.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository txnRepo;
    private final BankAccountRepository accountRepo;

    public TransactionServiceImpl(TransactionRepository txnRepo, BankAccountRepository accountRepo) {
        this.txnRepo = txnRepo;
        this.accountRepo = accountRepo;
    }

    @Override
    public Transaction create(String accountNumber, Transaction txn, String userId) {
        Account acc = accountRepo.findByAccountNumberAndUserId(accountNumber, userId)
                .orElseThrow(() ->
                        new AccountNotFoundException(String.format(AccountNotFoundException.STANDARD_EXCEPTION_MESSAGE, accountNumber)));

        if (txn.getType() == TransactionType.withdraw && acc.getBalance() < txn.getAmount()) {
            throw new UnprocessedTransactionException("Insufficient funds");
        }

        String txnId = "tan-" + UUID.randomUUID().toString().substring(0, 8);
        txn.setId(txnId);
        txn.setAccount(acc);
        txn.setUser(acc.getUser());

        // Adjust balance
        double newBalance = txn.getType() == TransactionType.deposit
                ? acc.getBalance() + txn.getAmount()
                : acc.getBalance() - txn.getAmount();
        acc.setBalance(newBalance);

        accountRepo.save(acc);
        return txnRepo.save(txn);
    }

    @Override
    public List<Transaction> list(String accountNumber, String userId) {
        accountRepo.findByAccountNumberAndUserId(accountNumber, userId)
                .orElseThrow(() ->
                        new AccountNotFoundException(String.format(AccountNotFoundException.STANDARD_EXCEPTION_MESSAGE, userId)));
        return txnRepo.findByAccountAccountNumber(accountNumber);
    }

    @Override
    public Transaction get(String accountNumber, String txnId, String userId) {
        accountRepo.findByAccountNumberAndUserId(accountNumber, userId)
                .orElseThrow(() -> new AccountNotFoundException(String.format(AccountNotFoundException.STANDARD_EXCEPTION_MESSAGE, accountNumber)));

        return txnRepo.findByIdAndAccountAccountNumber(txnId, accountNumber)
                .orElseThrow(() -> new TransactionNotFoundException(String.format(TransactionNotFoundException.STANDARD_EXCEPTION_MESSAGE, txnId)));
    }
}

