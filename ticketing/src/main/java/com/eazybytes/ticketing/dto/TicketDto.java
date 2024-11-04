package com.eazybytes.ticketing.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter @Getter
public class TicketDto {
    private String Name;
    private String ticketId;
    private String  origin;
    private String  destination;
    private double  amount;
    private LocalDate  expiryDate;
    private LocalDate purchasedDate;
}
