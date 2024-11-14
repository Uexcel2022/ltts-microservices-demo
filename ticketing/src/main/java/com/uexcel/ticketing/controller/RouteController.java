package com.uexcel.ticketing.controller;

import com.uexcel.ticketing.service.impl.client.BusFeignClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.RouteMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
public class RouteController {

    private final Logger logger = LoggerFactory.getLogger(RouteController.class);
    private final BusFeignClient busFeignClintFallback;
    @GetMapping("fetch-routes")
    ResponseEntity<List<RouteMatcher.Route>> fetchAllRoutes(){
        logger.debug("In route controller.fetchAllRoutes {}", "$$$$$$$$***********$$$");
        return null;

    }


}
