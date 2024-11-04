package com.eazybytes.ticketing.service.impl;

import com.eazybytes.ticketing.constants.ITicketConstants;
import com.eazybytes.ticketing.dto.ResponseDto;
import com.eazybytes.ticketing.exception.RouteException;
import com.eazybytes.ticketing.entity.Route;
import com.eazybytes.ticketing.exception.InvalidInputException;
import com.eazybytes.ticketing.exception.ResourceNoFoundException;
import com.eazybytes.ticketing.repositorty.RouteRepository;
import com.eazybytes.ticketing.service.IRouteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class IRouteServiceImpl implements IRouteService {
    private final RouteRepository routeRepository;
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
        if (routes.size() == 0) {
            throw new InvalidInputException("Empty route list");
        }
        List<Route> rt = new ArrayList<>();
        for (Route r : routes) {
        Route routeInDB =  routeRepository
                    .findByOriginAndDestination(r.getOrigin(), r.getDestination());
            if (routeInDB != null) {
                rt.add(routeInDB);
            }

        }
        if (rt.size() > 0) {
            throw new RouteException(rt);
        }

        routeRepository.saveAll(routes);

        return  new ResponseDto(ITicketConstants.statuscode_201,ITicketConstants.msg_201_route);
    }

    /**
     * @param routes - routes
     * @return status and message
     */
    @Override
    public ResponseDto updatePrice(List<Route> routes) {
        if (routes.size() == 0) {
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
        return  new ResponseDto(ITicketConstants.statuscode_200,ITicketConstants.msg_200_update);

    }
}
