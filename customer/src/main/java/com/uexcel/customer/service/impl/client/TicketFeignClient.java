package com.uexcel.customer.service.impl.client;
import com.uexcel.customer.dto.TicketResponseDto;
import com.uexcel.customer.dto.PostTicketDto;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@FeignClient(value = "ticketing",fallback = TicketFeignClientFallback.class)
public interface TicketFeignClient {
    @PostMapping(value = "/api/create-ticket",consumes = "application/json")
    ResponseEntity<TicketResponseDto> createTicket(@RequestBody PostTicketDto postTicketDto,
                                                   @RequestHeader("saferideCorrelationId") String correlationId);
    @GetMapping("/api/fetch-ticket")
    ResponseEntity<TicketResponseDto> getTicket(@RequestParam String customerId,
                                              @RequestHeader("saferideCorrelationId") String correlationId);
}
