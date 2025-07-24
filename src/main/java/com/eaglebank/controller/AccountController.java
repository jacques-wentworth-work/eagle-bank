package com.eaglebank.controller;

import com.eaglebank.entity.Account;
import com.eaglebank.exception.AccessForbiddenException;
import com.eaglebank.mapper.BankAccountMapper;
import com.eaglebank.resource.BankAccountCreateRequest;
import com.eaglebank.resource.BankAccountResponse;
import com.eaglebank.resource.BankAccountUpdateRequest;
import com.eaglebank.service.account.AccountService;
import com.eaglebank.service.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final JwtService jwtService;
    private final AccountService accountService;


    @PostMapping
    public ResponseEntity<BankAccountResponse> createAccount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @Valid @RequestBody BankAccountCreateRequest request
    ) {
        String userId = getUserId(jwt);
        validateUser(userId, jwt);

        Account acc = BankAccountMapper.toEntity(request);
        Account saved = accountService.create(acc, userId);
        return new ResponseEntity<>(BankAccountMapper.toResponse(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BankAccountResponse>> listAccounts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        String userId = getUserId(jwt);
        validateUser(userId, jwt);

        List<Account> list = accountService.get(userId);
        return ResponseEntity.ok(list.stream().map(BankAccountMapper::toResponse).toList());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> getAccount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @PathVariable String accountNumber) {
        String userId = getUserId(jwt);
        validateUser(userId, jwt);

        Account acc = accountService.get(accountNumber, userId);
        return ResponseEntity.ok(BankAccountMapper.toResponse(acc));
    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> updateAccount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @PathVariable String accountNumber,
            @RequestBody BankAccountUpdateRequest request) {
        String userId = getUserId(jwt);
        validateUser(userId, jwt);

        Account acc = BankAccountMapper.toEntity(request);
        Account updated = accountService.update(accountNumber, acc, userId);
        return ResponseEntity.ok(BankAccountMapper.toResponse(updated));
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable String accountNumber,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        String userId = getUserId(jwt);
        validateUser(userId, jwt);

        accountService.delete(accountNumber, userId);
        return ResponseEntity.noContent().build();
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

