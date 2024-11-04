package com.eazybytes.ticketing.service.impl;

import com.eazybytes.ticketing.entity.BusRoute;
import com.eazybytes.ticketing.dto.CheckinDto;
import com.eazybytes.ticketing.dto.ResponseDto;
import com.eazybytes.ticketing.entity.Ticket;
import com.eazybytes.ticketing.exception.InvalidInputException;
import com.eazybytes.ticketing.exception.ResourceNoFoundException;
import com.eazybytes.ticketing.repositorty.BusRouteRepository;
import com.eazybytes.ticketing.repositorty.TicketRepository;
import com.eazybytes.ticketing.service.ICheckinService;
import com.eazybytes.ticketing.service.ITicketService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class CheckinServiceImpl implements ICheckinService {
    private final TicketRepository ticketRepository;
    private final BusRouteRepository busRouteRepository;
    private final ITicketService ticketService;

    public CheckinServiceImpl(TicketRepository ticketRepository, BusRouteRepository
            busRouteRepository, ITicketService ticketService) {

        this.ticketRepository = ticketRepository;
        this.busRouteRepository = busRouteRepository;
        this.ticketService = ticketService;
    }
    
    /**
     * @param checkinDto - will hold ticketId and busId
     * @return - Returns status and message
     */
    public ResponseDto checkinValidation(CheckinDto checkinDto){

        Ticket ticket = ticketRepository.findById(checkinDto.getTicketId())
                .orElseThrow(()->new ResourceNoFoundException(
                        "Ticket","ticketId",checkinDto.getTicketId()));


        ticketService.checkTicketStatus(ticket,ticketRepository);

        /**
         * Get bus and check if the bus is enlisted on the route on the ticket
         * */
        List<BusRoute> buses = busRouteRepository.findBusRouteByBusId(checkinDto.getBusId());

                if(buses.isEmpty()){
                    throw new ResourceNoFoundException("BusRoute","busId",checkinDto.getBusId());
                }
        List<BusRoute> isValidRoute = buses.stream()
                .filter(busRout->busRout.getRouteId().equals(ticket.getRoutId())
        ).toList();

        if(isValidRoute.isEmpty()){
            throw new InvalidInputException(
                    "The ticketId "+ checkinDto.getTicketId()+" is not for this route");
        }

        ticket.setStatus(ITicketService.USE);
        ticket.setBusId(checkinDto.getBusId());
        ticket.setUsedDate(new Date());
        ticketRepository.save(ticket);
        return new ResponseDto(200,"Checkin successful. Thank you for using our services.");
    }


}
