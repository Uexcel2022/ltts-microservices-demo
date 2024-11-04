package com.eazybytes.ticketing.dto;

import com.eazybytes.ticketing.entity.Route;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter @Setter
public class RouteResponseDto {
    private Route route;
    private List<String> busId;
}
