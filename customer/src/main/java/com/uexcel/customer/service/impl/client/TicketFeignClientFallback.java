package com.uexcel.customer.service.impl.client;

import com.uexcel.customer.dto.BuyTicketDto;
import com.uexcel.customer.dto.BuyTicketResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TicketFeignClientFallback implements TicketFeignClient{

    @Override
    public ResponseEntity<BuyTicketResponseDto> createTicket(BuyTicketDto buyTicketDto, String correlationId) {
        return null;
    }
}
