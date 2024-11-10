package com.uexcel.customer.controller;
import com.uexcel.customer.dto.BuyTicketDto;
import com.uexcel.customer.dto.BuyTicketResponseDto;
import com.uexcel.customer.service.impl.client.TicketFeignClient;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TicketController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final TicketFeignClient ticketFeignClientFallback;
    @PostMapping("/buy-ticket")
    public ResponseEntity<BuyTicketResponseDto> buyTicket(@RequestBody BuyTicketDto buyTicketDto,
                                                                @RequestHeader("saferideCorrelationId") String correlationId ) {
        logger.debug("saferideCorrelation-id found: {}", correlationId);
     BuyTicketResponseDto td = ticketFeignClientFallback.createTicket(buyTicketDto,correlationId).getBody();
        return new ResponseEntity<>(td, HttpStatusCode.valueOf(td.getStatus()));
    }
}
