package com.uexcel.ticketing.service.impl;

import com.uexcel.ticketing.constants.ITicketConstants;
import com.uexcel.ticketing.dto.*;
import com.uexcel.ticketing.entity.BusRoute;
import com.uexcel.ticketing.entity.Route;
import com.uexcel.ticketing.exception.BusRouteException;
import com.uexcel.ticketing.exception.InvalidRouteBusIdException;
import com.uexcel.ticketing.exception.ResourceNoFoundException;
import com.uexcel.ticketing.repositorty.BusRouteRepository;
import com.uexcel.ticketing.repositorty.RouteRepository;
import com.uexcel.ticketing.service.IBusRouteService;
import com.uexcel.ticketing.service.impl.client.BusFeignClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class IBusRouteServiceImpl implements IBusRouteService {
    private final BusRouteRepository busRouteRepository;
    private final RouteRepository routeRepository;
    private  final BusFeignClient busFeignClintFallback;
    /**
     * @param busOrRouteList - list of [buses] (routes) to be added on [routes] (buses)
     * @param correlationId  - request race logging id
     * @return response
     */
    @Override
    @Transactional
    public ResponseDto createBusRoutes(List<BusRoute> busOrRouteList,String correlationId) {
        List<String> busRouteIds = new ArrayList<>();
        for (BusRoute busRoute : busOrRouteList) {
            ResponseEntity<BusDto> busDto = busFeignClintFallback.fetchBus(busRoute.getBusId(),correlationId);
            if (busDto == null) {
//                throw new ResourceNoFoundException("Bus","busId",busRoute.getBusId());
                busRouteIds.add(busRoute.getBusId());
            }
        }

        for (BusRoute busRoute : busOrRouteList) {
            Route route = routeRepository.findByRouteId(busRoute.getRouteId());
            if (route == null) {
                busRouteIds.add(String.valueOf(busRoute.getRouteId()));
//                throw new ResourceNoFoundException("Route","routeId",busRoute.getBusId());
            }
        }

         if (busRouteIds.size() > 0) {
          throw  new InvalidRouteBusIdException(busRouteIds);
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
