package com.uexcel.customer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FundTransferDto {
    private long payerWalletId;
    private long payeeWalletId;
    private  double amount;
}
