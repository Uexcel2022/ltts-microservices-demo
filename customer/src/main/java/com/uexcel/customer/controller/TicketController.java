package com.uexcel.customer.controller;
import com.uexcel.customer.dto.*;
import com.uexcel.customer.entity.Customer;
import com.uexcel.customer.repository.CustomerRepository;
import com.uexcel.customer.service.ITicketService;
import com.uexcel.customer.service.impl.client.BusFeignClient;
import com.uexcel.customer.service.impl.client.TicketFeignClient;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TicketController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final TicketFeignClient ticketFeignClientFallback;
    private final BusFeignClient busFeignClientFallback;
    private final ITicketService iTicketService;
    private final CustomerRepository customerRepository;

    @PostMapping("/buy-ticket")
    public ResponseEntity<TicketResponseDto> buyTicket(@RequestBody BuyTicketDto buyTicketDto,
                                                       @RequestHeader("saferideCorrelationId") String correlationId ) {
        logger.debug("saferideCorrelation-id found: {}", correlationId);

        ResponseEntity<Route> rt = busFeignClientFallback
                .getRoute(buyTicketDto.getRouteId(),correlationId);


        if(rt == null){
            return ResponseEntity.status(404).body(new TicketResponseDto(
                    iTicketService.getTime(),404,"Not Found",
                    "Route not found given input data routeId: "+ buyTicketDto.getRouteId(),null,null));
        }

        TicketResponseDto bt =  iTicketService.
                validateWalletBalance(buyTicketDto.getWalletId(),rt.getBody());
        if(bt.getStatus()!= 200){
            return ResponseEntity.status(bt.getStatus()).body(bt);
        }

     ResponseEntity<TicketResponseDto> btr =
             ticketFeignClientFallback.createTicket(iTicketService.postTicket(bt.getCustomerId(),rt.getBody()),correlationId);


     if (btr == null || btr.getBody().getStatus() == 500){
      TicketResponseDto dto = iTicketService.pressingPayment(rt.getBody().getPrice(),buyTicketDto.getWalletId());
      if(dto != null){
          logger.debug("Failed: Payment Refund Failed walletId: {}", buyTicketDto.getWalletId());
      }
        return interServerError();
     }

     if (btr.getBody().getStatus()==200){
        TicketResponseDto dto = iTicketService.pressingPayment(rt.getBody().getPrice(),buyTicketDto.getWalletId());
        btr.getBody().setStatus(302); //feignClient throw exception on status 302 on binding level
        if(dto != null){
            logger.debug("Existing Ticket: Payment Refund Failed walletId: {}", buyTicketDto.getWalletId());
        }
     }

        logger.debug("saferideCorrelation-id found: {}", correlationId);
        btr.getBody().setCustomerId(null); //customer id no longer needed
        btr.getBody().getTicket().get(0).setRouteId(null); //not needed here
        return new ResponseEntity<>(btr.getBody(), HttpStatusCode.valueOf(btr.getBody().getStatus()));
    }

    @GetMapping("/fetch-ticket")
    public ResponseEntity<TicketResponseDto> getTicket(@RequestParam String customerId,
                                                       @RequestHeader("saferideCorrelationId") String correlationId) {
        ResponseEntity<TicketResponseDto> rt = ticketFeignClientFallback.getTicket(customerId,correlationId);
        if (rt == null || rt.getBody().getStatus() == 500){
            return interServerError();
        }

        Customer c = customerRepository.findById(customerId).orElse(null);
        if(c == null){
           return ResponseEntity.status(404).body(
                   new TicketResponseDto(iTicketService.getTime(),404,"Not Found",
                    "Custer not found given input data customerId: " +customerId,null,null));
        }
        List<TicketDto> ticketDtoList = rt.getBody()
                .getTicket().stream().peek((el)->el.setName(c.getFirstName()+" "+c.getLastName())).toList();
        rt.getBody().setTicket(ticketDtoList);

        logger.debug("getTicket: saferideCorrelation-id found {}", correlationId);
        return ResponseEntity.status(rt.getBody().getStatus()).body(rt.getBody());
    }

    private ResponseEntity<TicketResponseDto> interServerError(){
        return ResponseEntity.status(500).body(
                new TicketResponseDto(iTicketService.getTime(),500,"Internal Server Error",
                        "Unable to process request. Please try again in few minutes time.",null,null));
    }

}
