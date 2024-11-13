package com.uexcel.ticketing.controller;

import com.uexcel.ticketing.dto.*;
import com.uexcel.ticketing.service.ITicketService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final ITicketService iTicketService;

    public TicketController(ITicketService iTicketService) {
        this.iTicketService = iTicketService;
    }

    @PostMapping("/create-ticket")
    @RateLimiter(name  ="createTicket",fallbackMethod = "createTicketFallBack")
    public ResponseEntity<BuyTicketResponseDto> createTicket(@RequestBody PostTicketDto postTicketDto,
                                                                   @RequestHeader("saferideCorrelationId") String correlationId) {
        BuyTicketResponseDto bt = iTicketService.createTicket(postTicketDto,correlationId);
        logger.debug("createTicket: saferideCorrelation-id found {}", correlationId);
        return ResponseEntity.status(bt.getStatus()).body(bt);
    }

    private ResponseEntity<String> createTicketFallBack(Throwable throwable) {
        return ResponseEntity.status(429).body("Too many requests");
    }


    @GetMapping("/fetch-ticket")
    public ResponseEntity<List<TicketDto>> getTicket(@RequestParam String customerId) {
       return ResponseEntity.ok().body(iTicketService.getTicket(customerId));
    }

    @PutMapping("/cancel-ticket")
    public ResponseEntity<ResponseDto> cancelTicket(String ticketId){
        boolean success = iTicketService.cancelTicket(ticketId);
        if(success) {
            return ResponseEntity.ok().body(new ResponseDto(200,
                    "Ticket cancelled successfully."));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(417, "Request failed."));
    }
}
