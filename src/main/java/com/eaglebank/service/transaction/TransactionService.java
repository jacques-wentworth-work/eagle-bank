package com.eaglebank.service.transaction;

import com.eaglebank.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction create(String accountNumber, Transaction txn, String userId);
    List<Transaction> list(String accountNumber, String userId);
    Transaction get(String accountNumber, String txnId, String userId);
}

