package com.uexcel.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter @Getter
public class TicketDto{
    private String Name;
    private String ticketId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String  origin;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String  destination;
    private double  amount;
    private LocalDate  expiryDate;
    private LocalDate purchasedDate;
}
