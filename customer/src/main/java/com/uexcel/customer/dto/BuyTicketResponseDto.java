package com.uexcel.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyTicketResponseDto {
    private Date date;
    private int status;
    private String description;
    private String message;
    private TicketDto ticket;
}

