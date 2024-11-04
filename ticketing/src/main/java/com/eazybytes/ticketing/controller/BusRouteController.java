package com.eazybytes.ticketing.controller;

import com.eazybytes.ticketing.dto.BusResponseDto;
import com.eazybytes.ticketing.dto.ResponseDto;
import com.eazybytes.ticketing.dto.RouteResponseDto;
import com.eazybytes.ticketing.entity.BusRoute;
import com.eazybytes.ticketing.service.IBusRouteService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
public class BusRouteController {
   private final Logger logger = LoggerFactory.getLogger(BusRouteController.class);
    private final IBusRouteService busRouteService;
    @PostMapping("/create-bus-route")
    public ResponseEntity<ResponseDto> manyBusToOneRoute(@RequestBody
                                                         List<BusRoute> busRoute){
        ResponseDto responseDto  = busRouteService.createBusRoutes(busRoute);
        logger.debug("BusRouteController.manyBusToOneRoute: {}", responseDto);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/get-bus-routes")
    public ResponseEntity<BusResponseDto> getBusRoutes(@RequestParam String busId){
        BusResponseDto busResp =  busRouteService.getBusRouteByBusId(busId);
        logger.debug("BusRouteController.getBusRoutes: {}", busResp);
        return ResponseEntity.ok().body(busResp);
    }

    @GetMapping("/get-route-buses")
    public ResponseEntity<RouteResponseDto> getRouteBuses(@RequestParam long routeId){
        RouteResponseDto routeResponseDto =  busRouteService.getBusRouteByRouteId(routeId);
        logger.debug("BusRouteController.getRouteBuses: {}", routeResponseDto);
        return ResponseEntity.ok().body(routeResponseDto);
    }
}
