package com.eazybytes.ticketing.service.impl.client;

import com.eazybytes.ticketing.dto.BusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("bus")
public interface BusFeignClient {
    @GetMapping("/api/fetch-bus")
    ResponseEntity<BusDto> fetchBus(@RequestParam String busId);
}
