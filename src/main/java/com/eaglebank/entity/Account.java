package com.eaglebank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "ACCOUNT_DETAIL",
        uniqueConstraints = @UniqueConstraint(columnNames = "accountNumber"))
public class Account {

    @Id
    private String accountNumber;

    private String name;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String sortCode = "10-10-10";

    private Double balance;

    private String currency = "GBP";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;

    @PrePersist
    public void onCreate() {
        createdTimestamp = LocalDateTime.now();
        balance = 0.0;
    }

    @PreUpdate
    public void onUpdate() {
        updatedTimestamp = LocalDateTime.now();
    }
}
