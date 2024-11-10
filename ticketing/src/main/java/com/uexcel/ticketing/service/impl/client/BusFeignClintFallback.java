package com.uexcel.ticketing.service.impl.client;

import com.uexcel.ticketing.dto.BusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BusFeignClintFallback implements BusFeignClient {
    @Override
    public ResponseEntity<BusDto> fetchBus(String busId, String correlationId) {
        return null;
    }
}
