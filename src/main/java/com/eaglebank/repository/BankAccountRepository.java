package com.eaglebank.repository;

import com.eaglebank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<Account, String> {
    List<Account> findByUserId(String userId);
    Optional<Account> findByAccountNumberAndUserId(String accountNumber, String userId);
    boolean existsByAccountNumber(String accountNumber);
}
