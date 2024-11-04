package com.eazybytes.ticketing.service.impl;


import com.eazybytes.ticketing.dto.BuyTicketDto;
import com.eazybytes.ticketing.dto.TicketDto;
import com.eazybytes.ticketing.dto.WalletDto;
import com.eazybytes.ticketing.dto.WalletTransactionDto;
import com.eazybytes.ticketing.entity.Route;
import com.eazybytes.ticketing.entity.Ticket;
import com.eazybytes.ticketing.exception.ExceptionFail;
import com.eazybytes.ticketing.exception.InvalidInputException;
import com.eazybytes.ticketing.exception.ResourceNoFoundException;
import com.eazybytes.ticketing.mapper.TicketMapper;
import com.eazybytes.ticketing.repositorty.RouteRepository;
import com.eazybytes.ticketing.repositorty.TicketRepository;
import com.eazybytes.ticketing.service.ITicketService;
import com.eazybytes.ticketing.service.impl.client.CustomerFeignClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements ITicketService {
    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;
    private CustomerFeignClient customerFeignClient;

    /**
     * @param buyTicketDto - holding customer and route information
     * @return - Returns ticked info
     */

    @Override
    @Transactional
    public TicketDto createTicket(BuyTicketDto buyTicketDto) {
        Route route = routeRepository.findById(buyTicketDto.getRouteId())
                .orElseThrow(()->new ResourceNoFoundException(
                        "Route","routeId",Long.toString(buyTicketDto.getRouteId()))
                );

        WalletDto walletDto = customerFeignClient
                .fetchWallet(buyTicketDto.getWalletId()).getBody();

     Ticket tk = ticketRepository
             .findByRoutIdAndCustomerIdAndStatusIgnoreCase (
                     buyTicketDto.getRouteId(), walletDto.getCustomerId(),VALID);
     if (tk != null) {
        throw  new InvalidInputException(
                "You have unused ticket on this route. TicketId: " + tk.getTicketId()
        );
     }

        if(walletDto.getBalance() < route.getPrice()){
            throw new  RuntimeException("Your wallet balance is insufficient.");
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

       boolean success = customerFeignClient.updateWallet(wt).getBody();
        if(success) {
            Ticket ticket = new Ticket();
            ticket.setCustomerId(walletDto.getCustomerId());
            ticketRepository.save(TicketMapper.mapToTicket(ticket, route));
            TicketDto ticketDto = TicketMapper.mapToTicketDto(ticket, route);
            ticketDto.setName(buyTicketDto.getName());
            return ticketDto;
        }
        throw new  ExceptionFail("Request was not successful.");
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
        ticket.stream().forEach(tk -> {
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
//        WalletDto walletDto = customerFeignClient
//                .fetchWallet();


//        walletBalance += ticket.getAmount();
        ticket.setStatus(REFUND);
        ticketRepository.save(ticket);
        return true;
    }


}
