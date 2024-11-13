package com.uexcel.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class BuyTicketResponseDto {
    private Date timestamp;
    private int status;
    private String description;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TicketDto ticket;
}
