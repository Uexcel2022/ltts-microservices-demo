package com.uexcel.bus.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter @Getter
public class AddRoutesToBus {
    private String busCode;
    private List<Long> routeId;
}
