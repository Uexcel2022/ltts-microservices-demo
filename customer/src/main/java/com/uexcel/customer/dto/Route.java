package com.uexcel.customer.dto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter

public class Route{
    private long routeId;
    private String region;
    private String origin;
    private String destination;
    private double price;
}
