package com.uexcel.customer.service.impl.client;

import com.uexcel.customer.dto.Route;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bus",fallback = busFeignClientFallback.class)
public interface BusFeignClient {
    @GetMapping(value = "/api/fetch-route",consumes = "application/json")
    ResponseEntity<Route> getRoute(@RequestParam Long routeId,
                                   @RequestHeader("saferideCorrelationId") String correlationId);
}
