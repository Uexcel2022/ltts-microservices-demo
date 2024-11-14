package com.uexcel.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class TicketResponseDto {
    private Date timestamp;
    private int status;
    private String description;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TicketDto> ticket;
}
