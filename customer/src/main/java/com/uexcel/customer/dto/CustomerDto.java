package com.uexcel.customer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerDto {
    private String customerId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    @JsonIgnore
    private String password;
    private WalletDto walletDto;
}
