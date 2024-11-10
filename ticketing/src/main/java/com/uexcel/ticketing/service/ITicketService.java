package com.uexcel.ticketing.service;

import com.uexcel.ticketing.dto.BuyTicketDto;
import com.uexcel.ticketing.dto.BuyTicketResponseDto;
import com.uexcel.ticketing.dto.TicketDto;
import com.uexcel.ticketing.entity.Ticket;
import com.uexcel.ticketing.exception.InvalidInputException;
import com.uexcel.ticketing.repositorty.TicketRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ITicketService {

     String REFUND = "refunded", USE = "used", EXPIRE = "expired", VALID = "valid";

    /**
     * @param buyTicketDto  - holding customer and route information
     * @param correlationId - logging trace id
     * @return - Returns ticked and request status info
     */

    BuyTicketResponseDto createTicket(BuyTicketDto buyTicketDto, String correlationId);

    /**
     * @param customerId - customer ID
     * @return Returns ticket info with TicketDto
     */
    List<TicketDto> getTicket(String customerId);

    /**
     * @param ticketId - ticket id
     * @return Returns boolean values indicating success or fail
     */
    boolean cancelTicket(String ticketId);


    /**
     * this validation methods are shared by the two services
     */
    default String formatDate(Date usedDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        return formatter.format(usedDate);
    }

    default void checkTicketStatus(Ticket ticket, TicketRepository ticketRepository) {

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


    }