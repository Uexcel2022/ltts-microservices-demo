package com.eazybytes.ticketing.controller;

import com.eazybytes.ticketing.dto.BuyTicketDto;
import com.eazybytes.ticketing.dto.ResponseDto;
import com.eazybytes.ticketing.dto.TicketDto;
import com.eazybytes.ticketing.service.ITicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {

    private final ITicketService iTicketService;

    public TicketController(ITicketService iTicketService) {
        this.iTicketService = iTicketService;
    }

    @PostMapping("/create-ticket")
    public ResponseEntity<TicketDto> createTicket(@RequestBody BuyTicketDto buyTicketDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(iTicketService.createTicket(buyTicketDto));
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
