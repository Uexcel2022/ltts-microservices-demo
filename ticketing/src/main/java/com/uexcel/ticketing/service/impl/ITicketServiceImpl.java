package com.uexcel.ticketing.service.impl;


import com.uexcel.ticketing.dto.*;
import com.uexcel.ticketing.entity.Route;
import com.uexcel.ticketing.entity.Ticket;
import com.uexcel.ticketing.exception.ResourceNoFoundException;
import com.uexcel.ticketing.mapper.TicketMapper;
import com.uexcel.ticketing.repositorty.RouteRepository;
import com.uexcel.ticketing.repositorty.TicketRepository;
import com.uexcel.ticketing.service.ITicketService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
public class ITicketServiceImpl implements ITicketService {
    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;

    private final Logger logger = LoggerFactory.getLogger(ITicketServiceImpl.class.getName());

    /**
     * @param postTicketDto  - holding customer and route information
     * @param correlationId - logging trace id
     * @return - Returns ticked info
     */

    @Override
    @Transactional
    public BuyTicketResponseDto createTicket(PostTicketDto postTicketDto, String correlationId) {

     Ticket tk = ticketRepository
             .findByRoutIdAndCustomerIdAndStatusIgnoreCase (
                     postTicketDto.getRoutId(), postTicketDto.getCustomerId(),VALID);

        TicketDto ticketDto = new TicketDto();

     if (tk != null) {
         logger.debug("createTicket:existing ticket: saferideCorrelation-id found: {}", correlationId);
         return new BuyTicketResponseDto(new Date(),
                 200,"Ok",
                 "You have unused ticket on this routed",
                 TicketMapper.mapToTicketDto(tk,ticketDto,postTicketDto));
     }

     Ticket newTicket = ticketRepository.save(TicketMapper.mapToTicket(new Ticket(),postTicketDto));

      if (newTicket != null && newTicket.getTicketId() != null){
          logger.debug("createTicket: new ticket: saferideCorrelation-id found: {}", correlationId);
          return new BuyTicketResponseDto(new Date(),
                  201,"Created",
                  "Ticket created successfully.",
                  TicketMapper.mapToTicketDto(newTicket,ticketDto,postTicketDto));

      }
        logger.debug("createTicket:failed: saferideCorrelation-id found: {}", correlationId);
         return new BuyTicketResponseDto(new Date(),
                500,"Fail", "Fail: ",null);
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
            ticketDtoList.add(TicketMapper.mapToTicketDto(tk, new TicketDto()));
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
