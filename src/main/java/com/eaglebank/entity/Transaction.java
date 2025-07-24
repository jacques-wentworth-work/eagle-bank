package com.eaglebank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Transaction {

    @Id
    private String id;

    private Double amount;

    private String currency = "GBP";

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String reference;

    private LocalDateTime createdTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void onCreate() {
        createdTimestamp = LocalDateTime.now();
    }
}
