package com.uexcel.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class BuyTicketResponseDto {
    private String timestamp;
    private int status;
    private String description;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customerId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TicketDto ticket;
}

