package com.eazybytes.ticketing.service.impl;

import com.eazybytes.ticketing.constants.ITicketConstants;
import com.eazybytes.ticketing.dto.*;
import com.eazybytes.ticketing.entity.BusRoute;
import com.eazybytes.ticketing.entity.Route;
import com.eazybytes.ticketing.exception.ResourceNoFoundException;
import com.eazybytes.ticketing.repositorty.BusRouteRepository;
import com.eazybytes.ticketing.repositorty.RouteRepository;
import com.eazybytes.ticketing.service.IBusRouteService;
import com.eazybytes.ticketing.service.impl.client.BusFeignClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class IBusRouteServiceImpl implements IBusRouteService {
    private final BusRouteRepository busRouteRepository;
    private final RouteRepository routeRepository;
    private  final BusFeignClient busFeignClient;
    /**
     * @param busOrRouteList - list of [buses] (routes) to be added on [routes] (buses)
     * @return response
     */
    @Override
    @Transactional
    public ResponseDto createBusRoutes(List<BusRoute> busOrRouteList) {

        for (BusRoute busRoute : busOrRouteList) {
            BusDto busDto = busFeignClient.fetchBus(busRoute.getBusId()).getBody();
            if (busDto == null) {
                throw new  ResourceNoFoundException("Bus","busId",busDto.getBusId());
            }
        }

        List<BusRoute> existSBusRoute = new ArrayList<>();
        for (BusRoute busRoute : busOrRouteList) {
         boolean exists =   busRouteRepository
                    .existsBusRouteByBusIdAndRouteId(busRoute.getBusId(), busRoute.getRouteId());
         if (exists) {
             existSBusRoute.add(busRoute);
         }
        }

        if (!existSBusRoute.isEmpty()) {
           throw  new BusRouteException(existSBusRoute);
        }

      busRouteRepository.saveAll(busOrRouteList);
        return new ResponseDto(
                ITicketConstants.statuscode_201,ITicketConstants.msg_201_bust_route
        );
    }

    /**
     * @param busId - bus ID
     * @return bus and its routes
     */
    @Override
    public BusResponseDto getBusRouteByBusId(String busId) {
        List<BusRoute> busRouteList  = busRouteRepository.findBusRouteByBusId(busId);
        if(busRouteList.isEmpty()){
            new ResourceNoFoundException("Bust and Route","routeId",busId);
        }
        BusResponseDto busResponseDto = new BusResponseDto();
        List<Route> routeList = new ArrayList<>();
        busResponseDto.setBusId(busRouteList.stream().findFirst().get().getBusId());

        busRouteList.forEach(busRoute -> {
            Route route = routeRepository
                    .findById(busRoute.getRouteId()).orElse(null);
            routeList.add(route);
        });

       busResponseDto.setRoute(routeList);

       return busResponseDto;

    }

    /**
     * @param routeId - route ID
     * @return route and buses enlisted on it
     */
    @Override
    public RouteResponseDto getBusRouteByRouteId(long routeId) {
       List<BusRoute> busRoute =  busRouteRepository.findBusRouteByRouteId(routeId);
       if(busRoute.isEmpty()){
           new ResourceNoFoundException("Bust and Route","routeId",Long.toString(routeId));
       }
       Route route = routeRepository.findById(routeId).orElseThrow(
               ()->new ResourceNoFoundException("Route","routeId",Long.toString(routeId)));
       RouteResponseDto routeResponseDto = new RouteResponseDto();
        routeResponseDto.setRoute(route);
       List <String> buses = new ArrayList<>();
        busRoute.forEach(br -> buses.add(br.getBusId()));
        routeResponseDto.setBusId(buses);
        return routeResponseDto;
    }
}
