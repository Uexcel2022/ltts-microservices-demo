package com.uexcel.ticketing.service.impl;


import com.uexcel.ticketing.dto.*;
import com.uexcel.ticketing.entity.Ticket;
import com.uexcel.ticketing.mapper.TicketMapper;
import com.uexcel.ticketing.repositorty.TicketRepository;
import com.uexcel.ticketing.service.ITicketService;
import com.uexcel.ticketing.service.impl.client.BusFeignClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
public class ITicketServiceImpl implements ITicketService {
    private final TicketRepository ticketRepository;
    private final BusFeignClient busFeignClientFallback;
    private final Logger logger = LoggerFactory.getLogger(ITicketServiceImpl.class.getName());

    /**
     * @param postTicketDto  - holding customer and route information
     * @param correlationId - logging trace id
     * @return - Returns ticked info
     */

    @Override
    @Transactional
    public TicketResponseDto createTicket(PostTicketDto postTicketDto, String correlationId) {

     Ticket tk = ticketRepository
             .findByRoutIdAndCustomerIdAndStatusIgnoreCase (
                     postTicketDto.getRoutId(), postTicketDto.getCustomerId(),VALID);

        TicketDto ticketDto = new TicketDto();
        List<TicketDto> tickets = new ArrayList<>();

     if (tk != null) {
         logger.debug("createTicket:existing ticket: saferideCorrelation-id found: {}", correlationId);
         tickets.add(TicketMapper.mapToTicketDto(tk,ticketDto,postTicketDto));
         return new TicketResponseDto(new Date(),
                 302,"Found", "You have unused ticket on this routed.",null, tickets);
     }

     Ticket newTicket = ticketRepository.save(TicketMapper.mapToTicket(new Ticket(),postTicketDto));

      if (newTicket != null && newTicket.getTicketId() != null){
          logger.debug("createTicket: new ticket: saferideCorrelation-id found: {}", correlationId);
          tickets.add(TicketMapper.mapToTicketDto(newTicket,ticketDto,postTicketDto));
          return new TicketResponseDto(new Date(),
                  201,"Created",
                  "Ticket created successfully.",null, tickets);

      }
        logger.debug("createTicket:failed: saferideCorrelation-id found: {}", correlationId);
         return new TicketResponseDto(new Date(),
                500,"Fail", "Fail: ",null,null);
    }

    /**
     * @param customerId - customer ID
     * @return Returns ticket info with TicketDto
     */
    @Override
    public TicketResponseDto getTicket(String customerId, String correlationId) {
        List<Ticket> ticket = ticketRepository
                .findByCustomerIdAndStatusIgnoreCase(customerId,VALID);

        if(ticket.isEmpty()){
            return new TicketResponseDto(
                    new Date(),404,"Not Found",
                    "Valid Ticket not found given input data customerId: "+customerId,null,null);
        }
        List<TicketDto> ticketDtoList = new ArrayList<>();
        List<Integer> skippedRoutes = new ArrayList<>();
        ticket.forEach(tk -> {
           ResponseEntity<Route> route = busFeignClientFallback.getRoute(tk.getRoutId(),correlationId);
           if(route != null) {
               ticketDtoList.add(TicketMapper.mapToTicketDto(tk, route.getBody(), new TicketDto()));
           }else {
               skippedRoutes.add(1);
           }
        });

        if(!skippedRoutes.isEmpty()){
            return new TicketResponseDto(
                    new Date(),200,"Ok",
                    "Some routes may have been skipped. Refresh to get origin and destination.",null,ticketDtoList);
        }
        return new TicketResponseDto(
                new Date(),200,"Ok","Request processed successfully.",null,ticketDtoList);
    }

    /**
     * @param ticketId - ticket id
     * @return  ticketResponseDto will hold status and ticket information
     */
    @Override
    public TicketResponseDto cancelTicket(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if(ticket == null){
            return new TicketResponseDto( new Date(),404,
                    "Not Found","Ticket not found given input date ticketId: "+ticketId,null,null);
        }

        if(!ticket.getPurchasedDate().plusDays(366).isAfter(LocalDate.now())){
            ticket.setStatus(EXPIRE);
            ticketRepository.save(ticket);
                return new TicketResponseDto( new Date(),400,
                        "Bad Request","Ticket "+ticketId+" has expired",null,null);
        }

        if (ticket.getStatus().equalsIgnoreCase(USE)) {
            return new TicketResponseDto( new Date(), 400, "Bad Request",
                    "The ticket was used on " + formatDate(ticket.getUsedDate()),null,null);
        }

        if (ticket.getStatus().equalsIgnoreCase(REFUND)) {
           return new   TicketResponseDto( new Date(), 400, "Bad Request",
                    "The ticket was refunded on " + formatDate(ticket.getUpdatedAt()),null,null);
        }

        ticket.setStatus(REFUND);
        ticketRepository.save(ticket);
        TicketDto ticketDto = new TicketDto();
        ticketDto.setAmount(ticket.getAmount());
        List<TicketDto> ticketDtoList = new ArrayList<>();
        ticketDtoList.add(ticketDto);
        return new TicketResponseDto( new Date(),200,
                "Ok","Ticket canceled successfully",ticket.getCustomerId(),ticketDtoList);

    }


}
