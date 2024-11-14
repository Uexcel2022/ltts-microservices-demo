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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.uexcel.ticketing.service.ITicketService.*;

@Service
public class CheckinServiceImpl implements ICheckinService {
    private final TicketRepository ticketRepository;
    private final BusFeignClient busFeignClientFallback;

    public CheckinServiceImpl(TicketRepository ticketRepository,
                              BusFeignClient busFeignClientFallback) {
        this.ticketRepository = ticketRepository;
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

        checkTicketStatus(ticket,ticketRepository);

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


    private void checkTicketStatus(Ticket ticket, TicketRepository ticketRepository) {

        if(!ticket.getPurchasedDate().plusDays(366).isAfter(LocalDate.now())){
            ticket.setStatus(EXPIRE);
            ticketRepository.save(ticket);
            throw  new InvalidInputException("Ticket has expired.");
        }

        if (ticket.getStatus().equalsIgnoreCase(USE)) {
            throw new InvalidInputException(
                    "The ticket was used on " + formatDate(ticket.getUsedDate()));
        }

        if (ticket.getStatus().equalsIgnoreCase(REFUND)) {
            throw new InvalidInputException(
                    "The ticket was refunded on " + formatDate(ticket.getUpdatedAt()));
        }
    }

    private String formatDate(Date usedDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        return formatter.format(usedDate);
    }
}
