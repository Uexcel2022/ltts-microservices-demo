package com.uexcel.ticketing.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter

public class Route {
    private long routeId;
    private String region;
    private String origin;
    private String destination;
    private double price;
    private Set<Bus> buses = new HashSet<>();
}
