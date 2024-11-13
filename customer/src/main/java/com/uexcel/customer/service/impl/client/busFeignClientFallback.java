package com.uexcel.customer.service.impl.client;

import com.uexcel.customer.dto.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class busFeignClientFallback implements BusFeignClient{
    @Override
    public ResponseEntity<Route> getRoute(Long routeId, String correlationId) {
        return null;
    }
}
