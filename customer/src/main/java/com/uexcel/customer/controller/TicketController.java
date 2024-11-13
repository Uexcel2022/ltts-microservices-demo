package com.uexcel.customer.controller;
import com.uexcel.customer.dto.BuyTicketDto;
import com.uexcel.customer.dto.BuyTicketResponseDto;
import com.uexcel.customer.dto.Route;
import com.uexcel.customer.service.impl.TicketService;
import com.uexcel.customer.service.impl.client.BusFeignClient;
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
    private final BusFeignClient busFeignClientFallback;
    private final TicketService ticketService;
    @PostMapping("/buy-ticket")
    public ResponseEntity<BuyTicketResponseDto> buyTicket(@RequestBody BuyTicketDto buyTicketDto,
                                                                @RequestHeader("saferideCorrelationId") String correlationId ) {
        logger.debug("saferideCorrelation-id found: {}", correlationId);

        ResponseEntity<Route> rt = busFeignClientFallback
                .getRoute(buyTicketDto.getRouteId(),correlationId);


        if(rt == null){
            return ResponseEntity.status(404).body(new BuyTicketResponseDto(
                    ticketService.getTime(),404,"Not Found",
                    "Route not found given input data routeId: "+ buyTicketDto.getRouteId(),null,null));
        }

        BuyTicketResponseDto bt =  ticketService.
                validateWalletBalance(buyTicketDto.getWalletId(),rt.getBody());
        if(bt.getStatus()!= 200){
            return ResponseEntity.status(bt.getStatus()).body(bt);
        }

     ResponseEntity<BuyTicketResponseDto> btr =
             ticketFeignClientFallback.createTicket(ticketService.postTicket(bt.getCustomerId(),rt.getBody()),correlationId);


     if (btr == null || btr.getBody().getStatus() == 500){
      BuyTicketResponseDto dto = ticketService.pressingPayment(rt.getBody().getPrice(),buyTicketDto.getWalletId());
      if(dto != null){
          logger.debug("Failed: Payment Refund Failed walletId: {}", buyTicketDto.getWalletId());
      }
        return ResponseEntity.status(500).body(
                 new BuyTicketResponseDto(ticketService.getTime(),500,"Internal Server Error",
                         "Unable to process request. Please try again in few minutes time.",null,null));
     }

     if (btr.getBody().getStatus()==200){
        BuyTicketResponseDto dto = ticketService.pressingPayment(rt.getBody().getPrice(),buyTicketDto.getWalletId());
        if(dto != null){
            logger.debug("Existing Ticket: Payment Refund Failed walletId: {}", buyTicketDto.getWalletId());
        }
     }

        logger.debug("saferideCorrelation-id found: {}", correlationId);
        btr.getBody().setCustomerId(null); //customer id no longer needed
        btr.getBody().getTicket().setRouteId(null); //not needed here
        return new ResponseEntity<>(btr.getBody(), HttpStatusCode.valueOf(btr.getBody().getStatus()));
    }


}
