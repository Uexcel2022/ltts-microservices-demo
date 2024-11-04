package com.uexcel.customer.service.impl.client;

import com.uexcel.customer.dto.BuyTicketDto;
import com.uexcel.customer.dto.TicketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("ticketing")
public interface TicketFeignClient {
    @PostMapping(value = "/api/create-ticket",consumes = "application/json")
    ResponseEntity<TicketDto> createTicket(@RequestBody BuyTicketDto buyTicketDto);
}
