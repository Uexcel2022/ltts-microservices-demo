package com.eazybytes.ticketing.dto;
import lombok.Getter;
import lombok.Setter;


@Setter @Getter
public class WalletDto {
    private Long walletId;
    private String customerId;
    private double balance;
}
