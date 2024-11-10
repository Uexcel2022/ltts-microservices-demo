package com.uexcel.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TicketDto ticket;
}
