package com.uexcel.ticketing.mapper;

import com.uexcel.ticketing.dto.PostTicketDto;
import com.uexcel.ticketing.dto.Route;
import com.uexcel.ticketing.dto.TicketDto;
import com.uexcel.ticketing.entity.Ticket;
import com.uexcel.ticketing.service.ITicketService;

import java.time.LocalDate;


public class TicketMapper {
  public static TicketDto mapToTicketDto(Ticket ticket, Route route, TicketDto dto) {
       dto.setTicketId(ticket.getTicketId());
       dto.setRouteId(route.getRouteId());
       dto.setOrigin(route.getOrigin());
       dto.setDestination(route.getDestination());
       dto.setAmount(ticket.getAmount());
       dto.setPurchasedDate(ticket.getPurchasedDate());
       dto.setRouteId(ticket.getRoutId());
       dto.setExpiryDate(
               ticket.getPurchasedDate().plusDays(366));
       return dto;
   }

    public static TicketDto mapToTicketDto(Ticket ticket,TicketDto dto,PostTicketDto postTicketDto) {
        dto.setName(postTicketDto.getCustomerName());
        dto.setTicketId(ticket.getTicketId());
        dto.setRouteId(ticket.getRoutId());
        dto.setAmount(ticket.getAmount());
        dto.setPurchasedDate(ticket.getPurchasedDate());
        dto.setRouteId(ticket.getRoutId());
        dto.setOrigin(postTicketDto.getOrigin());
        dto.setDestination(postTicketDto.getDestination());
        dto.setExpiryDate(
                ticket.getPurchasedDate().plusDays(366));
        return dto;
    }

   public static  Ticket mapToTicket(Ticket ticket, PostTicketDto postTicketDto) {
       ticket.setAmount(postTicketDto.getAmount());
       ticket.setPurchasedDate(LocalDate.now());
       ticket.setRoutId(postTicketDto.getRoutId());
       ticket.setCustomerId(postTicketDto.getCustomerId());
       ticket.setStatus(ITicketService.VALID);
       return ticket;
    }



}
