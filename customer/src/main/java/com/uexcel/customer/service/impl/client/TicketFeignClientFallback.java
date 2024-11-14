package com.uexcel.customer.service.impl.client;

import com.uexcel.customer.dto.TicketResponseDto;
import com.uexcel.customer.dto.PostTicketDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TicketFeignClientFallback implements TicketFeignClient{

    @Override
    public ResponseEntity<TicketResponseDto> createTicket(PostTicketDto postTicketDto, String correlationId) {
        return null;
    }

    @Override
    public ResponseEntity<TicketResponseDto> getTicket(String customerId, String correlationId) {
        return null;
    }
}
