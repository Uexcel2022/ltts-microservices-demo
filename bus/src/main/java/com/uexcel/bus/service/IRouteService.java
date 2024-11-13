package com.uexcel.bus.service;


import com.uexcel.bus.dto.AddBusToRoute;
import com.uexcel.bus.dto.AddRoutesToBus;
import com.uexcel.bus.dto.ResponseDto;
import com.uexcel.bus.entity.Route;

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

    /**
     * @param addBusToRoute  - will be containing routeId and buses to be added to the route
     * @return status and message
     */
    ResponseDto addBusToRute(AddBusToRoute addBusToRoute);

    /**
     * @param addRoutesToBus - will be containing busId and routes to be assigned to the bus
     * @return status and message
     */
    ResponseDto addRoutesToBus(AddRoutesToBus addRoutesToBus);

    /**
     * @param region - region to which a route belong
     * @return - routes details
     */
    List<Route>  fetchRouteRegion(String region);


    default boolean validateRoute(Route route) {
        return route.getDestination() != null &&  !route.getDestination().isEmpty() &&
                route.getOrigin() != null && !route.getOrigin().isEmpty() &&
                route.getRegion() != null && !route.getRegion().isEmpty() &&
                route.getPrice() >0;
    }
}
