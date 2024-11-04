package com.eazybytes.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
public class BusRouteId implements Serializable {
    @Id
    @Column(nullable = false)
    private String busId;
    @Id
    @Column(nullable = false)
    private long routeId;


}
