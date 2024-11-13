package com.uexcel.bus.service.impl;


import com.uexcel.bus.dto.AddBusToRoute;
import com.uexcel.bus.dto.AddRoutesToBus;
import com.uexcel.bus.dto.ResponseDto;
import com.uexcel.bus.entity.Bus;
import com.uexcel.bus.entity.Route;
import com.uexcel.bus.exception.InvalidInputException;
import com.uexcel.bus.exception.InvalidRouteBusIdException;
import com.uexcel.bus.exception.ResourceNoFoundException;
import com.uexcel.bus.exception.RouteException;
import com.uexcel.bus.repository.BusRepository;
import com.uexcel.bus.repository.RouteRepository;
import com.uexcel.bus.service.IRouteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class IRouteServiceImpl implements IRouteService {
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    /**
     * @return routes
     */
    @Override
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    /**
     * @param routeId - routId
     * @return specific route based on the id
     */
    @Override
    public Route getRouteById(long routeId) {
       Route route = routeRepository.findByRouteId(routeId);
       if (route == null) {
           throw new ResourceNoFoundException(
                   "Route","routeId",Long.toString(routeId)
           );
       }
       return route;
    }

    /**
     * @param routes - rute
     * @return status and messate
     */
    @Override
    public ResponseDto create(List<Route> routes) {
        if (routes.isEmpty()) {
            throw new InvalidInputException("Empty route list");
        }
        List<Route> rt = new ArrayList<>();
        for (Route r : routes) {
            if (!validateRoute(r)) {
                rt.add(r);
            }
        Route routeInDB =  routeRepository
                    .findByOriginAndDestination(r.getOrigin(), r.getDestination());
            if (routeInDB != null) {
                rt.add(routeInDB);
            }

        }
        if (!rt.isEmpty()) {
            throw new RouteException(rt);
        }

        routeRepository.saveAll(routes);

        return  new ResponseDto(201,"Route(s) created successfully.");
    }

    /**
     * @param routes - routes
     * @return status and message
     */
    @Override
    public ResponseDto updatePrice(List<Route> routes) {
        if (routes.isEmpty()) {
            throw new InvalidInputException("Empty route list");
        }
        for (Route r : routes) {
            Route routeInDB =  routeRepository.findById(r.getRouteId())
                    .orElseThrow(() -> new ResourceNoFoundException(
                            "Route","routeId",Long.toString(r.getRouteId())
                            )
                    );
            routeInDB.setPrice(r.getPrice());
            routeRepository.save(routeInDB);
        }
        return  new ResponseDto(200,"Route(s) updated successfully.");

    }

    /**
     * @param addBusToRoute  - will be containing routeId and buses to be added to the route
     * @return status and message
     */
    @Override
    public ResponseDto addBusToRute(AddBusToRoute addBusToRoute) {
        Set<Bus> buses = new HashSet<>();
        List<String> invalidBusIdorRouteIdList = new ArrayList<>();

        if(addBusToRoute == null || addBusToRoute.getBusCode().isEmpty()) {
            throw new InvalidInputException("Required route and list of buses");
        }

        Route route = getRouteById(addBusToRoute.getRouteId());
        if (route == null) {
            invalidBusIdorRouteIdList.add(Long.toString(addBusToRoute.getRouteId()));
        }

        addBusToRoute.getBusCode()
                .forEach(bus -> {
                    Optional<Bus> bs = busRepository.findByBusCode(bus);
                    if (bs.isPresent()) {
                        buses.add(bs.get());
                    }else {
                        invalidBusIdorRouteIdList.add(bus);
                    }
                });

        if(!invalidBusIdorRouteIdList.isEmpty()) {
           throw new InvalidRouteBusIdException(invalidBusIdorRouteIdList);
        }

        route.setBuses(buses);
        routeRepository.save(route);
        return new ResponseDto(200,"Buse(s) added to route successfully.");
    }

    /**
     * @param addRoutesToBus - will be containing busId and routes to be assigned to the bus
     * @return status and message
     */
    @Override
    public ResponseDto addRoutesToBus(AddRoutesToBus addRoutesToBus) {
        List<Route> routes = new ArrayList<>();
        Set<Bus> buses = new HashSet<>();
        List<String> invalidBusIdorRouteIdList = new ArrayList<>();

        if(addRoutesToBus == null || addRoutesToBus.getRouteId().isEmpty()) {
            throw new InvalidInputException("Required bus and list of routes");
        }

        Bus bus = busRepository.findByBusCode(addRoutesToBus.getBusCode()).orElse(null);

        if (bus==null) {
            invalidBusIdorRouteIdList.add(addRoutesToBus.getBusCode());
        }
        buses.add(bus);
        addRoutesToBus.getRouteId()
                .forEach(route -> {
                    Route rt = routeRepository.findByRouteId(route);
                    if (rt != null) {
                        rt.setBuses(buses);
                        routes.add(rt);
                    }else {
                        invalidBusIdorRouteIdList.add(Long.toString(route));
                    }
                });

        if(!invalidBusIdorRouteIdList.isEmpty()) {
            throw new InvalidRouteBusIdException(invalidBusIdorRouteIdList);
        }
        routeRepository.saveAll(routes);

        return new ResponseDto(200,"Route(s) added to bus successfully.");
    }

    /**
     * @param region - region to which a route belong
     * @return - routes details
     */
    @Override
    public List<Route> fetchRouteRegion(String region) {
        List<Route> routes = routeRepository.findByRegion(region);
        if (routes.isEmpty()) {
            throw new ResourceNoFoundException("Route","region",region);
        }
        return routes;
    }
}
