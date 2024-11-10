package com.uexcel.customer.controller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uexcel.customer.dto.BuyTicketDto;
import com.uexcel.customer.dto.BuyTicketResponseDto;
import com.uexcel.customer.service.impl.client.TicketFeignClient;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
     ResponseEntity<BuyTicketResponseDto> td = ticketFeignClientFallback.createTicket(buyTicketDto,correlationId);
     if (td == null){
        return ResponseEntity.status(500).body(
                 new BuyTicketResponseDto(new Date(),500,"Internal Server Error",
                         "Unable to process request. Please try again in few minutes time.",null));
     }
        return new ResponseEntity<>(td.getBody(), HttpStatusCode.valueOf(td.getBody().getStatus()));
    }
}
