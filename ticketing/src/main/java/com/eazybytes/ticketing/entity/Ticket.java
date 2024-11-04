package com.eazybytes.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;


@Getter @Setter
@Entity
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String ticketId;
    private long  routId;
    private String customerId;
    private double  amount;
    private String  status;
    @Column(updatable = false)
    private LocalDate purchasedDate;
    @Column(insertable = false)
    private Date usedDate;
    private String  busId;
}
