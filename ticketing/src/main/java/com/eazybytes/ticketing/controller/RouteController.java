package com.eazybytes.ticketing.controller;

import com.eazybytes.ticketing.constants.ITicketConstants;
import com.eazybytes.ticketing.dto.ResponseDto;
import com.eazybytes.ticketing.entity.Route;
import com.eazybytes.ticketing.service.IRouteService;
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
    @GetMapping("fetch-routes")
    ResponseEntity<List<Route>> fetchAllRoutes(){
        List<Route> routes = routeService.getAllRoutes();
        logger.debug("In route controller.fetchAllRoutes {}", "$$$$$$$$***********$$$");
        return ResponseEntity.ok().body(routes);
    }
    @GetMapping("/fetch-route")
    ResponseEntity<Route> fetchRute(@RequestParam long routeId){
        Route route = routeService.getRouteById(routeId);
        logger.debug("In route controller.fetchRute {}", "$$$$$$$$***********$$$");
        return ResponseEntity.ok().body(route);
    }

    @PostMapping("create-route")
    public ResponseEntity<ResponseDto> createRoute(@RequestBody List<Route> routes){
         ResponseDto responseDto = routeService.create(routes);
        logger.debug("In route controller.createRoute {}", routes);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PutMapping("update-route")
    public ResponseEntity<ResponseDto> updateRoute(@RequestBody List<Route> routes){
        ResponseDto responseDto = routeService.updatePrice(routes);
        logger.debug("In route controller.updateRoute {}", routes);
        return ResponseEntity.status(200).body(responseDto);
    }

}
