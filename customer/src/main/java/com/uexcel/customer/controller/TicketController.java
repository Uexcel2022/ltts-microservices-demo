package com.uexcel.customer.controller;
import com.uexcel.customer.dto.*;
import com.uexcel.customer.entity.Customer;
import com.uexcel.customer.entity.Wallet;
import com.uexcel.customer.repository.CustomerRepository;
import com.uexcel.customer.repository.WalletRepository;
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
    private final WalletRepository walletRepository;

    @PostMapping("/buy-ticket")
    public ResponseEntity<TicketResponseDto> buyTicket(@RequestBody BuyTicketDto buyTicketDto,
                                                       @RequestHeader("saferideCorrelationId") String correlationId ) {
        logger.debug("saferideCorrelation-id found: {}", correlationId);

        ResponseEntity<Route> rt = busFeignClientFallback
                .getRoute(buyTicketDto.getRouteId(),correlationId);


        if(rt == null){
            return ResponseEntity.status(404).body(new TicketResponseDto(
                    iTicketService.getTime(),404,"Not Found",
                    "Route not found given input data routeId: "+ buyTicketDto.getRouteId(),
                    "uri=/api/buy-ticket",null,null));
        }

        TicketResponseDto bt =  iTicketService.
                validateWalletBalance(buyTicketDto.getWalletId(),rt.getBody());
        if(bt.getStatus()!= 200){
            bt.setApiPath("uri=/api/buy-ticket");
            return ResponseEntity.status(bt.getStatus()).body(bt);
        }

     TicketResponseDto btr =
             ticketFeignClientFallback.createTicket(iTicketService.postTicket(bt.getCustomerId(),rt.getBody()),correlationId);


     if (btr == null || btr.getStatus() == 500){
      TicketResponseDto dto = iTicketService.pressingPayment(rt.getBody().getPrice(),buyTicketDto.getWalletId());
      if(dto != null){
          logger.debug("Failed: Payment Refund Failed walletId: {}", buyTicketDto.getWalletId());
      }
        return interServerError("uri=/api/buy-ticket");
     }

     if (btr.getStatus()==302){
        TicketResponseDto dto = iTicketService.pressingPayment(rt.getBody().getPrice(),buyTicketDto.getWalletId());
        if(dto != null){
            logger.debug("Existing Ticket: Payment Refund Failed walletId: {}", buyTicketDto.getWalletId());
        }
     }

        logger.debug("saferideCorrelation-id found: {}", correlationId);
        btr.setCustomerId(null); //customer id no longer needed
        btr.getTicket().get(0).setRouteId(null); //not needed here
        return new ResponseEntity<>(btr, HttpStatusCode.valueOf(btr.getStatus()));
    }

    @GetMapping("/fetch-ticket")
    public ResponseEntity<TicketResponseDto> getTicket(@RequestParam String customerId,
                                                       @RequestHeader("saferideCorrelationId") String correlationId) {
        TicketResponseDto rt = ticketFeignClientFallback.getTicket(customerId,correlationId);
        if (rt == null || rt.getStatus() == 500){
            return interServerError("uri=api/fetch-ticket");
        }

        Customer c = customerRepository.findById(customerId).orElse(null);
        if(c == null){
           return ResponseEntity.status(404).body(
                   new TicketResponseDto(iTicketService.getTime(),404,"Not Found",
                    "Custer not found given input data customerId: " +customerId,
                           "uri=api/fetch-ticket",null,null));
        }
        List<TicketDto> ticketDtoList = rt
                .getTicket().stream().peek((el)->el.setName(c.getFirstName()+" "+c.getLastName())).toList();
        rt.setTicket(ticketDtoList);

        logger.debug("getTicket: saferideCorrelation-id found {}", correlationId);
        return ResponseEntity.status(rt.getStatus()).body(rt);
    }

    @PutMapping("/cancel-ticket")
    public ResponseEntity<TicketResponseDto> cancelTicket(@RequestParam String ticketId,
                                                           @RequestHeader("saferideCorrelationId") String correlationId) {
        TicketResponseDto rt = ticketFeignClientFallback.cancelTicket(ticketId, correlationId);
        if(rt == null){
            return interServerError("uri=/api/cancel-ticket");
        }

        if(rt.getStatus()==200){
            Wallet wallet = walletRepository.findByCustomerId(rt.getCustomerId()).orElse(null);
            if(wallet == null){
                logger.debug("CancelTicket: Failed: wallet not found : Payment Refund Failed customerId: {}"
                        ,rt.getCustomerId() );
                return ResponseEntity.status(404).body(
                        new TicketResponseDto(iTicketService.getTime(),404,"Not Found",
                                "Walled not found given input data customerId: " +
                                        "" +rt.getCustomerId(),"uri=/api/cancel-ticket",null,null));
            }else {

                TicketResponseDto dto =
                        iTicketService.pressingPayment(rt.getTicket().get(0).getAmount(), wallet.getWalletId());
                if(dto != null){
                    logger.debug("CancelTicket: Walled update failure: Payment Refund Failed walletId: {}",
                            wallet.getWalletId());
                }
            }
        }
        rt.setCustomerId(null); //not needed any more
        rt.setTicket(null);
        return ResponseEntity.status(rt.getStatus()).body(rt);
    }


    private ResponseEntity<TicketResponseDto> interServerError(String path){
        logger.debug("{Unable to process request. Please try again in few minutes time}");
        return ResponseEntity.status(500).body(
                new TicketResponseDto(iTicketService.getTime(),500,"Internal Server Error",
                        "Unable to process request. Please try again in few minutes time.",path,null,null));
    }

}
