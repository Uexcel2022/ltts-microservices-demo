package com.uexcel.customer.service;

import com.uexcel.customer.dto.TicketResponseDto;
import com.uexcel.customer.dto.PostTicketDto;
import com.uexcel.customer.dto.Route;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface ITicketService {

    /**
     * @param walletId  - wallet ID
     * @param route  - will hold route information
     * @return
     */
    TicketResponseDto validateWalletBalance(Long walletId, Route route);

    /**
     * @param amount - ticket price
     * @param walletId - wallet ID
     * @return
     */
    TicketResponseDto pressingPayment(double amount, long walletId);


    /**
     * @param customerId  - customer ID
     * @param route - will hold route information
     * @return
     */
    PostTicketDto postTicket(String customerId, Route route);

    default String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSZ");
        return sdf.format(new Date());
    }

}
