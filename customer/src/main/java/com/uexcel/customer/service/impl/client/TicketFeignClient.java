package com.uexcel.customer.service.impl.client;

import com.uexcel.customer.dto.BuyTicketDto;

import com.uexcel.customer.dto.BuyTicketResponseDto;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(value = "ticketing",fallback = TicketFeignClientFallback.class)
public interface TicketFeignClient {
    @PostMapping(value = "/api/create-ticket",consumes = "application/json")
    ResponseEntity<BuyTicketResponseDto> createTicket(@RequestBody BuyTicketDto buyTicketDto,
                                                            @RequestHeader("saferideCorrelationId") String correlationId);
}
