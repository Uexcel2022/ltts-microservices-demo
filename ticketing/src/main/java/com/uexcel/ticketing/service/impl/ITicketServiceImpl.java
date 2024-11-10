package com.uexcel.ticketing.service.impl;


import com.uexcel.ticketing.dto.*;
import com.uexcel.ticketing.entity.Route;
import com.uexcel.ticketing.entity.Ticket;
import com.uexcel.ticketing.exception.ResourceNoFoundException;
import com.uexcel.ticketing.mapper.TicketMapper;
import com.uexcel.ticketing.repositorty.RouteRepository;
import com.uexcel.ticketing.repositorty.TicketRepository;
import com.uexcel.ticketing.service.ITicketService;
import com.uexcel.ticketing.service.impl.client.CustomerFeignClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ITicketServiceImpl implements ITicketService {
    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;
    private CustomerFeignClient customerFeignClientFallback;

    /**
     * @param buyTicketDto  - holding customer and route information
     * @param correlationId - logging trace id
     * @return - Returns ticked info
     */

    @Override
    @Transactional
    public BuyTicketResponseDto createTicket(BuyTicketDto buyTicketDto, String correlationId) {
        Route route = routeRepository.findById(buyTicketDto.getRouteId())
                .orElseThrow(()->new ResourceNoFoundException(
                        "Route","routeId",Long.toString(buyTicketDto.getRouteId()))
                );

        ResponseEntity<WalletDto> w = customerFeignClientFallback
                .fetchWallet(buyTicketDto.getWalletId(),correlationId);
        if(w == null){

          return   new BuyTicketResponseDto(new Date(),
                    404,"Not Found","Wallet not found given input data walletId: "
                    + buyTicketDto.getWalletId(),null);
        }
        WalletDto walletDto = w.getBody();

     Ticket tk = ticketRepository
             .findByRoutIdAndCustomerIdAndStatusIgnoreCase (
                     buyTicketDto.getRouteId(), walletDto.getCustomerId(),VALID);
     if (tk != null) {
         TicketDto ticketDto = new TicketDto();
         ticketDto.setTicketId(tk.getTicketId());
         ticketDto.setName(buyTicketDto.getName());
         ticketDto.setAmount(tk.getAmount());
         ticketDto.setExpiryDate(tk.getPurchasedDate().plusDays(366));
         ticketDto.setPurchasedDate(tk.getPurchasedDate());
         Route rt = routeRepository.findByRouteId(tk.getRoutId());
         if (rt == null) {
             return new BuyTicketResponseDto(new Date(),
                     417,"Fail",
                     "Failed to retrieve ticked origin and destination",ticketDto);
         }
         ticketDto.setDestination(rt.getDestination());
         ticketDto.setOrigin(rt.getOrigin());

         return new BuyTicketResponseDto(new Date(),
                 302,"Found",
                 "You have unused ticket on this routed",ticketDto);
     }

        if(walletDto.getBalance() < route.getPrice()){
            return new BuyTicketResponseDto(new Date(),
                    400,"Bad Request",
                    "You have insufficient balance",null);
        }

        double newBal= walletDto.getBalance() - route.getPrice();
        walletDto.setBalance(newBal);

        //update wallet
        WalletTransactionDto wt = new WalletTransactionDto();
        wt.setAccountDescription("ticket");
        wt.setAmount(-route.getPrice());
        wt.setAccountNumber(Long.toString(walletDto.getWalletId()));
        wt.setWalletId(walletDto.getWalletId());
        wt.setTransactionType("ticket");
        wt.setWalletId(walletDto.getWalletId());

        ResponseEntity<Boolean>  success =
       customerFeignClientFallback.updateWallet(wt,correlationId);

        if(success != null && success.getBody()) {
            Ticket ticket = new Ticket();
            ticket.setCustomerId(walletDto.getCustomerId());
            ticketRepository.save(TicketMapper.mapToTicket(ticket, route));
            TicketDto ticketDto = TicketMapper.mapToTicketDto(ticket, route);
            ticketDto.setName(buyTicketDto.getName());
           return new BuyTicketResponseDto(new Date(),
                    200,"OK","Request processed successfully.",ticketDto);

        }
         return new BuyTicketResponseDto(new Date(),
                417,"Fail",
                "Ticket payment processing failed. walletId: "+buyTicketDto.getWalletId(),null);
    }

    /**
     * @param customerId - customer ID
     * @return Returns ticket info with TicketDto
     */
    @Override
    public List<TicketDto> getTicket(String customerId) {
        List<Ticket> ticket = ticketRepository
                .findByCustomerIdAndStatusIgnoreCase(customerId,VALID);

        if(ticket.isEmpty()){
            throw  new ResourceNoFoundException("Ticket","customerId",customerId);
        }
        List<TicketDto> ticketDtoList = new ArrayList<>();
        ticket.forEach(tk -> {
            Route route = routeRepository.findByRouteId(tk.getRoutId());
            ticketDtoList.add(TicketMapper.mapToTicketDto(tk, route));
        });
        return ticketDtoList;
    }

    /**
     * @param ticketId - ticket id
     * @return Returns boolean values indicating success or fail
     */
    @Override
    public boolean cancelTicket(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()->new ResourceNoFoundException("Ticket","ticketId",ticketId));

        checkTicketStatus(ticket,ticketRepository);
//        WalletDto walletDto = customerFeignClientFallback
//                .fetchWallet();


//        walletBalance += ticket.getAmount();
        ticket.setStatus(REFUND);
        ticketRepository.save(ticket);
        return true;
    }


}
