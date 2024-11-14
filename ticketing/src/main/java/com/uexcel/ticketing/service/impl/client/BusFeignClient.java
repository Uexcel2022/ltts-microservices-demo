package com.uexcel.ticketing.service.impl.client;

import com.uexcel.ticketing.dto.Route;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bus",fallback = BusFeignClientFallback.class)
public interface BusFeignClient {
    @GetMapping("/api/fetch-route")
    ResponseEntity<Route> fetchRoute(@RequestParam Long routeId,
                                     @RequestHeader("saferideCorrelationId") String correlationId);

    @GetMapping("/api/fetch-route")
    ResponseEntity<Route> getRoute(@RequestParam Long routeId,
                                          @RequestHeader("saferideCorrelationId") String correlationId);
}
