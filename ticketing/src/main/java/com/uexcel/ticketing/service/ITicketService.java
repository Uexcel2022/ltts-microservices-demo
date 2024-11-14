package com.uexcel.ticketing.service;

import com.uexcel.ticketing.dto.TicketResponseDto;
import com.uexcel.ticketing.dto.PostTicketDto;
import com.uexcel.ticketing.entity.Ticket;
import com.uexcel.ticketing.exception.InvalidInputException;
import com.uexcel.ticketing.repositorty.TicketRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public interface ITicketService {

     String REFUND = "refunded", USE = "used", EXPIRE = "expired", VALID = "valid";

    /**
     * @param postTicketDto  - will be bolding information to process ticket
     * @param correlationId - logging trace id
     * @return - Returns ticked and request status info
     */

    TicketResponseDto createTicket(PostTicketDto postTicketDto, String correlationId);

    /**
     * @param customerId - customer ID
     * @param  correlationId - logging trace id
     * @return Returns ticket info with TicketDto
     */
    TicketResponseDto getTicket(String customerId, String correlationId);

    /**
     * @param ticketId - ticket id
     * @return Returns boolean values indicating success or fail
     */
    TicketResponseDto cancelTicket(String ticketId);


    /**
     * this validation methods are shared by the two services
     */
    default String formatDate(Date usedDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        return formatter.format(usedDate);
    }

    }
