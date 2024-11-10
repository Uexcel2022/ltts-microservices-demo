package com.uexcel.ticketing.mapper;

import com.uexcel.ticketing.dto.TicketDto;
import com.uexcel.ticketing.entity.Route;
import com.uexcel.ticketing.entity.Ticket;
import com.uexcel.ticketing.service.ITicketService;

import java.time.LocalDate;


public class TicketMapper {
  public static TicketDto mapToTicketDto(Ticket ticket, Route route) {
       TicketDto dto = new TicketDto();
       dto.setTicketId(ticket.getTicketId());
       dto.setOrigin(route.getOrigin());
       dto.setDestination(route.getDestination());
       dto.setAmount(route.getPrice());
       dto.setPurchasedDate(ticket.getPurchasedDate());
       dto.setExpiryDate(
               ticket.getPurchasedDate().plusDays(366));
       return dto;
   }

   public static  Ticket mapToTicket(Ticket ticket, Route route) {
       ticket.setAmount(route.getPrice());
       ticket.setPurchasedDate(LocalDate.now());
       ticket.setRoutId(route.getRouteId());
       ticket.setStatus(ITicketService.VALID);
       return ticket;
    }



}
