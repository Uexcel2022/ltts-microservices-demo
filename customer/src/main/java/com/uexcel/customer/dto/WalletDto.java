package com.uexcel.customer.dto;
import lombok.Getter;
import lombok.Setter;


@Setter @Getter
public class WalletDto {
    private Long walletId;
    private double balance;
    private String customerId;
}
