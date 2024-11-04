package com.uexcel.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String customerId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String emailAddress;
    @Column(unique = true)
    private String phoneNumber;
    private String password;
    private String status;

}
