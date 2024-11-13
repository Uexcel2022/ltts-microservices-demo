package com.uexcel.customer.service.impl.client;

import com.uexcel.customer.dto.BuyTicketResponseDto;
import com.uexcel.customer.dto.PostTicketDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TicketFeignClientFallback implements TicketFeignClient{

    @Override
    public ResponseEntity<BuyTicketResponseDto> createTicket(PostTicketDto postTicketDto, String correlationId) {
        return null;
    }
}
