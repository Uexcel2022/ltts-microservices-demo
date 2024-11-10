package com.uexcel.ticketing.service;

import com.uexcel.ticketing.dto.BusResponseDto;
import com.uexcel.ticketing.dto.ResponseDto;
import com.uexcel.ticketing.dto.RouteResponseDto;
import com.uexcel.ticketing.entity.BusRoute;

import java.util.List;

public interface IBusRouteService {

    /**
     * @param busOrRouteList - list of [buses] (routes) to be added on [routes] (buses)
     * @param correlationId - request trace logging id
     * @return response
     */
    ResponseDto createBusRoutes(List<BusRoute> busOrRouteList,String correlationId);

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
