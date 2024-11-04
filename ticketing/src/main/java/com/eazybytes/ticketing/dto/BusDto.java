package com.eazybytes.ticketing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BusDto {
    private String busId;
    private String busName;
    private int capacity;
}
