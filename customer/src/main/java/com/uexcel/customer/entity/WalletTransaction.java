package com.uexcel.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private long walletId;
    @Column(nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private String accountDescription;
    @Column(nullable = false)
    private String transactionType;
    private double amount;
    private String Date;
}
