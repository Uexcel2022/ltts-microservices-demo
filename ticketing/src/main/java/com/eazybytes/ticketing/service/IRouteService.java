package com.eazybytes.ticketing.service;

import com.eazybytes.ticketing.dto.ResponseDto;
import com.eazybytes.ticketing.entity.Route;

import java.util.List;

public interface IRouteService {

    /**
     * @return  routes
     */
    List<Route> getAllRoutes();

    /**
     * @param routeId - routId
     * @return specific route based on the id
     */
    Route getRouteById(long routeId);

    /**
     * @param routes  - rute
     * @return  status and message
     */
    ResponseDto create(List<Route> routes);

    /**
     * @param routes - routes
     * @return status and message
     */
    ResponseDto updatePrice(List<Route> routes);
}
