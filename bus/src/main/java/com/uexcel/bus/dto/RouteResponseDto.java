package com.uexcel.bus.dto;

import com.uexcel.bus.entity.Route;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter @Setter
public class RouteResponseDto {
    private Route route;
    private List<String> busId;
}
