package com.eaglebank.service.transaction;

import com.eaglebank.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(String accountNumber, Transaction txn, String userId);
    List<Transaction> listTransactions(String accountNumber, String userId);
    Transaction getTransaction(String accountNumber, String txnId, String userId);
}

