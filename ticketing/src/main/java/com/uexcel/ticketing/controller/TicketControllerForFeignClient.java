package com.uexcel.ticketing.controller;

import com.uexcel.ticketing.dto.PostTicketDto;
import com.uexcel.ticketing.dto.TicketResponseDto;
import com.uexcel.ticketing.service.ITicketService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketControllerForFeignClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final ITicketService iTicketService;

    public TicketControllerForFeignClient(ITicketService iTicketService) {
        this.iTicketService = iTicketService;
    }

    @PostMapping("/create-ticket")
    @RateLimiter(name  ="createTicket",fallbackMethod = "createTicketFallBack")
    public TicketResponseDto createTicket(@RequestBody PostTicketDto postTicketDto,
                                                          @RequestHeader("saferideCorrelationId") String correlationId) {
        TicketResponseDto bt = iTicketService.createTicket(postTicketDto,correlationId);
        logger.debug("createTicket: saferideCorrelation-id found {}", correlationId);
        return bt;
    }

    @GetMapping("/fetch-ticket")
    public TicketResponseDto getTicket(@RequestParam String customerId,
                                                     @RequestHeader("saferideCorrelationId") String correlationId) {
        logger.debug("getTicket: saferideCorrelation-id found {}", correlationId);
       return iTicketService.getTicket(customerId,correlationId);
    }

    @PutMapping("/cancel-ticket")
    public TicketResponseDto cancelTicketFeignClient(@RequestParam String ticketId,
                                                     @RequestHeader("saferideCorrelationId") String correlationId){
        TicketResponseDto td = iTicketService.cancelTicket(ticketId);
        logger.debug("cancelTicketFeignClient: saferideCorrelation-id found {}", correlationId);
        return td;

    }
}
