package com.uexcel.ticketing.service.impl.client;

import com.uexcel.ticketing.dto.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BusFeignClientFallback implements BusFeignClient {
    @Override
    public ResponseEntity<Route> fetchRoute(Long routeId, String correlationId) {
        return null;
    }

    @Override
    public ResponseEntity<Route> getRoute(Long routeId, String correlationId) {
        return null;
    }
}
