package com.uexcel.bus.controller;
import com.uexcel.bus.dto.AddBusToRoute;
import com.uexcel.bus.dto.AddRoutesToBus;
import com.uexcel.bus.dto.ResponseDto;
import com.uexcel.bus.entity.Route;
import com.uexcel.bus.service.IRouteService;
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
public class RouteController {
   private final Logger logger = LoggerFactory.getLogger(RouteController.class);
    private final IRouteService routeService;
    @PostMapping("/create-route")
    public ResponseEntity<ResponseDto> createRoutes(@RequestBody List<Route> routes){
        ResponseDto responseDto  = routeService.create(routes);

        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/fetch-route")
    public ResponseEntity<Route> getRoute(@RequestParam Long routeId,
                                           @RequestHeader("saferideCorrelationId") String correlationId){
        Route route =  routeService.getRouteById(routeId);
        logger.debug("getRoutes: saferideCorrelation-id fund: {}", correlationId);
        return ResponseEntity.ok().body(route);
    }

    @GetMapping("/fetch-routes-region")
    public ResponseEntity<List<Route>> getRoutesByRegion(@RequestParam String region,
                                           @RequestHeader("saferideCorrelationId") String correlationId){
        List<Route> route =  routeService.fetchRouteRegion(region);
        logger.debug("getRoutesByRegion: saferideCorrelation-id fund: {}", correlationId);
        return ResponseEntity.ok().body(route);
    }

    @GetMapping("/fetch-routes")
    public ResponseEntity<List<Route>> getAllRoutes(){
        List<Route> route =  routeService.getAllRoutes();
        logger.debug("BusRouteController.getRouteBuses: {}", route);
        return ResponseEntity.ok().body(route);
    }

    @PostMapping("/add-bus-to-route")
    public ResponseEntity<ResponseDto> addBusToRoute(@RequestBody AddBusToRoute addBusToRoute){
        ResponseDto responseDto  = routeService.addBusToRute(addBusToRoute);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PostMapping("/add-route-to-bus")
    public ResponseEntity<ResponseDto> addRoutesToBuss(@RequestBody AddRoutesToBus addRoutesToBus){
        ResponseDto responseDto  = routeService.addRoutesToBus(addRoutesToBus);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PutMapping("/update-route")
    public ResponseEntity<ResponseDto> update(@RequestBody List<Route> route){
        ResponseDto responseDto = routeService.updatePrice(route);
        return ResponseEntity.status(200).body(responseDto);
    }
}
