package com.eazybytes.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


@Setter @Getter
public class WalletDto {
    private Long walletId;
    private String customerId;
    private double balance;
}
