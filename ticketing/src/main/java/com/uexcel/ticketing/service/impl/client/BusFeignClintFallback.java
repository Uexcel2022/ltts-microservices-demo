package com.uexcel.ticketing.service.impl.client;

import com.uexcel.ticketing.entity.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BusFeignClintFallback implements BusFeignClient {
    @Override
    public ResponseEntity<Route> fetchBus(String busId, String correlationId) {
        return null;
    }
}
