package com.uexcel.ticketing.service;

import com.uexcel.ticketing.dto.CheckinDto;
import com.uexcel.ticketing.dto.ResponseDto;
import com.uexcel.ticketing.entity.Ticket;
import com.uexcel.ticketing.exception.InvalidInputException;
import com.uexcel.ticketing.repositorty.TicketRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static com.uexcel.ticketing.service.ITicketService.*;


public interface ICheckinService {

    /**
     * @param checkinDto  - will hold ticketId and busId
     * @return Returns response status and message
     */
     ResponseDto checkinValidation(CheckinDto checkinDto,String correlationId);



}
