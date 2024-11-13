package com.uexcel.customer.service.impl;

import com.uexcel.customer.constants.ICustomerConstants;
import com.uexcel.customer.dto.*;
import com.uexcel.customer.entity.Customer;
import com.uexcel.customer.entity.Wallet;
import com.uexcel.customer.entity.WalletTransaction;
import com.uexcel.customer.exception.ResourceNotFoundException;
import com.uexcel.customer.repository.CustomerRepository;
import com.uexcel.customer.repository.WalletRepository;
import com.uexcel.customer.service.ICustomerService;
import com.uexcel.customer.service.IWalletService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Component
@Getter @Setter
@AllArgsConstructor
public class TicketService {
    private final WalletRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private WalletRepository walletRepository;
    private final IWalletService walletService;
    private final ICustomerService customerService;


    public BuyTicketResponseDto validateWalletBalance(Long walletId, Route route) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        if (wallet == null || wallet.getStatus().equals(ICustomerConstants.CUSTOMER_DEACTIVATED)) {
            return new BuyTicketResponseDto(
                    getTime(), 404, "Not Found",
                    "Wallet not found given input data walletId: " + walletId, null, null);
        }
        if (wallet.getBalance() < route.getPrice()) {
            return new BuyTicketResponseDto(
                    getTime(), 400, "Bad Request",
                    "Insufficient balance", null, null);
        }

        BuyTicketResponseDto bt = pressingPayment(-route.getPrice(), wallet.getWalletId());

        if (bt == null) {
            return new BuyTicketResponseDto(
                    getTime(), 200, "0k",
                    "Sufficient balance", wallet.getCustomerId(), null);
        }
        return bt;
    }


     public BuyTicketResponseDto pressingPayment(double amount, long walletId){
        WalletTransaction wt = new WalletTransaction();
        wt.setAmount(amount);
        wt.setWalletId(walletId);
        boolean success = walletService.updateWallet(wt);
        if(!success){
            if(amount < 0) {
                return new BuyTicketResponseDto(
                        getTime(), 417, "Fail",
                        "Payment processing fail", null, null);
            }
            return new BuyTicketResponseDto(
                    getTime(), 417, "Fail",
                    "Refund processing fail", null, null);
        }
        return null;
     }

    public PostTicketDto postTicket(String customerId,Route route) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow( ()-> new ResourceNotFoundException("Customer",customerId,customerId));
        PostTicketDto postTicketDto = new PostTicketDto();
        postTicketDto.setCustomerId(customerId);
        postTicketDto.setCustomerName(customer.getFirstName()+" "+customer.getLastName());
        postTicketDto.setOrigin(route.getOrigin());
        postTicketDto.setDestination(route.getDestination());
        postTicketDto.setAmount(route.getPrice());
        postTicketDto.setRoutId(route.getRouteId());
        postTicketDto.setPurchasedDate(LocalDate.now());
        return postTicketDto;
    }

    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSZ");
        return sdf.format(new Date());
    }


}
