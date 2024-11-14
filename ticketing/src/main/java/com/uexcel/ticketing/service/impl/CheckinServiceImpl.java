package com.uexcel.ticketing.service.impl;

import com.uexcel.ticketing.dto.Bus;
import com.uexcel.ticketing.dto.CheckinDto;
import com.uexcel.ticketing.dto.ResponseDto;
import com.uexcel.ticketing.dto.Route;
import com.uexcel.ticketing.entity.Ticket;
import com.uexcel.ticketing.exception.InvalidInputException;
import com.uexcel.ticketing.exception.ResourceNoFoundException;
import com.uexcel.ticketing.repositorty.TicketRepository;
import com.uexcel.ticketing.service.ICheckinService;
import com.uexcel.ticketing.service.ITicketService;
import com.uexcel.ticketing.service.impl.client.BusFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class CheckinServiceImpl implements ICheckinService {
    private final TicketRepository ticketRepository;
    private final ITicketService ticketService;
    private final BusFeignClient busFeignClientFallback;

    public CheckinServiceImpl(TicketRepository ticketRepository, ITicketService ticketService,
                              BusFeignClient busFeignClientFallback) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.busFeignClientFallback = busFeignClientFallback;
    }
    
    /**
     * @param checkinDto - will hold ticketId and busId
     * @return - Returns status and message
     */
    public ResponseDto checkinValidation(CheckinDto checkinDto,String correlationId){

        Ticket ticket = ticketRepository.findById(checkinDto.getTicketId())
                .orElseThrow(()->new ResourceNoFoundException(
                        "Ticket","ticketId",checkinDto.getTicketId()));

        ticketService.checkTicketStatus(ticket,ticketRepository);

        /**
         * Get bus and check if the bus is enlisted on the route on the ticket
         * */
        ResponseEntity<Route> route = busFeignClientFallback.fetchRoute(ticket.getRoutId(),correlationId);


                if(route==null){
                    throw new ResourceNoFoundException("Route","routeId",ticket.getTicketId());
                }
        List<Bus> isValidRoute = route.getBody().getBuses().stream()
                .filter(bs -> bs.getBusCode().equals(checkinDto.getBusCode())
        ).toList();

        if(isValidRoute.isEmpty()){
            throw new InvalidInputException(
                    "The ticketId "+ checkinDto.getTicketId()+" is not for this route.");
        }

        ticket.setStatus(ITicketService.USE);
        ticket.setBusId(checkinDto.getBusCode());
        ticket.setUsedDate(new Date());
        ticketRepository.save(ticket);
        return new ResponseDto(200,"Checkin successful. Thank you for using our services.");
    }


}
