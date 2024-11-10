package com.uexcel.ticketing.service.impl.client;

import com.uexcel.ticketing.dto.BusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bus",fallback = BusFeignClintFallback.class)
public interface BusFeignClient {
    @GetMapping("/api/fetch-bus")
    ResponseEntity<BusDto> fetchBus(@RequestParam String busId,
                                    @RequestHeader("saferideCorrelationId") String correlationId);
}
