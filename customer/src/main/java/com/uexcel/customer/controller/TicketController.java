package com.uexcel.customer.controller;

import com.uexcel.customer.dto.BuyTicketDto;
import com.uexcel.customer.dto.TicketDto;
import com.uexcel.customer.service.impl.client.TicketFeignClient;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TicketController {
    private final TicketFeignClient ticketFeignClient;
    @PostMapping("/buy-ticket")
    public TicketDto buyTicket(@RequestBody BuyTicketDto buyTicketDto) {
        return ticketFeignClient.createTicket(buyTicketDto).getBody();
    }
}
