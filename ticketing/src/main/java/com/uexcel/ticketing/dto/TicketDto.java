package com.uexcel.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Setter @Getter @ToString
public class TicketDto{
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long routeId;
    private String ticketId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String  origin;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String  destination;
    private double  amount;
    private LocalDate purchasedDate;
    private LocalDate  expiryDate;

}
