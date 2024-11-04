package com.uexcel.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter @Getter
public class Wallet extends BaseEntity {
    @Id
    private Long walletId;
    @Column(nullable = false, unique = true)
    private String customerId;
    private double balance;
    @JsonIgnore
    private String status;
}
