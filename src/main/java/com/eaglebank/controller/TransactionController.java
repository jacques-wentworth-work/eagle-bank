package com.eaglebank.controller;

import com.eaglebank.entity.Transaction;
import com.eaglebank.exception.AccessForbiddenException;
import com.eaglebank.mapper.TransactionMapper;
import com.eaglebank.resource.TransactionCreateRequest;
import com.eaglebank.resource.TransactionResponse;
import com.eaglebank.service.jwt.JwtService;
import com.eaglebank.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts/{accountNumber}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final JwtService jwtService;
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @PathVariable String accountNumber,
            @RequestBody TransactionCreateRequest request) {
        String userId = getUserId(jwt);
        validateUser(userId, jwt);

        Transaction txn = TransactionMapper.toEntity(request);
        Transaction saved = transactionService.createTransaction(accountNumber, txn, userId);
        return new ResponseEntity<>(TransactionMapper.toResponse(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> listTransactions(
            @PathVariable String accountNumber,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        String userId = getUserId(jwt);
        validateUser(userId, jwt);

        List<Transaction> txns = transactionService.listTransactions(accountNumber, userId);
        return ResponseEntity.ok(txns.stream().map(TransactionMapper::toResponse).toList());
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @PathVariable String accountNumber,
            @PathVariable String transactionId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        String userId = getUserId(jwt);
        validateUser(userId, jwt);

        Transaction txn = transactionService.getTransaction(accountNumber, transactionId, userId);
        return ResponseEntity.ok(TransactionMapper.toResponse(txn));
    }

    private String getUserId(String jwt) {
        return jwtService.extractUsername(jwt.substring("Bearer ".length()));
    }

    private void validateUser(String userId, String jwt) {
        String tokenUserId = jwtService.extractUsername(jwt.substring("Bearer ".length()));
        if (!tokenUserId.equals(userId)) {
            throw new AccessForbiddenException("You are not authorized to access this user's information");
        }
    }
}

