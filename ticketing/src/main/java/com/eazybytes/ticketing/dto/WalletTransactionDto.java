package com.eazybytes.ticketing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WalletTransactionDto {
    private long walletId;
    private String accountNumber;
    private String accountDescription;
    private String transactionType;
    private double amount;
}
