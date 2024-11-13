package com.uexcel.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter @Setter @ToString
public class PostTicketDto{
    private long  routId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customerId;
    private String customerName;
    private String origin;
    private String destination;
    private double  amount;
    @Column(updatable = false)
    private LocalDate purchasedDate;
}
