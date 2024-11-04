package com.eazybytes.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyTicketDto {
    private long routeId;
    private long walletId;
    private String name;
}
