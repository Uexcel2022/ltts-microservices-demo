package com.eazybytes.ticketing.service;

import com.eazybytes.ticketing.dto.BusResponseDto;
import com.eazybytes.ticketing.dto.ResponseDto;
import com.eazybytes.ticketing.dto.RouteResponseDto;
import com.eazybytes.ticketing.entity.BusRoute;

import java.util.List;

public interface IBusRouteService {

    /**
     * @param busOrRouteList - list of [buses] (routes) to be added on [routes] (buses)
     * @return response
     */
    ResponseDto createBusRoutes(List<BusRoute> busOrRouteList);


    /**
     * @param busId - bus ID
     * @return bus and its routes
     */
    BusResponseDto getBusRouteByBusId(String busId);

    /**
     * @param routeId - route ID
     * @return routes and buses enlisted on the route
     */
    RouteResponseDto getBusRouteByRouteId(long routeId);
}
