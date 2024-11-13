package com.uexcel.ticketing.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;


@Getter @Setter @ToString
public class PostTicketDto {
    private long  routId;
    private String customerId;
    private String origin;
    private String destination;
    private String customerName;
    private double  amount;
    private String  status;
    @Column(updatable = false)
    private LocalDate purchasedDate;
}
