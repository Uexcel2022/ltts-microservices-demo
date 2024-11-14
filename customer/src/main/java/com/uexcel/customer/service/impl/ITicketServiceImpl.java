package com.uexcel.customer.service.impl;

import com.uexcel.customer.constants.ICustomerConstants;
import com.uexcel.customer.dto.*;
import com.uexcel.customer.entity.Customer;
import com.uexcel.customer.entity.Wallet;
import com.uexcel.customer.entity.WalletTransaction;
import com.uexcel.customer.exception.ResourceNotFoundException;
import com.uexcel.customer.repository.CustomerRepository;
import com.uexcel.customer.repository.WalletRepository;
import com.uexcel.customer.service.ITicketService;
import com.uexcel.customer.service.IWalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@AllArgsConstructor
public class ITicketServiceImpl implements ITicketService {
    private final CustomerRepository customerRepository;
    private WalletRepository walletRepository;
    private final IWalletService walletService;


    /**
     * @param walletId - wallet ID
     * @param route    - will hold route information
     * @return
     */
    @Override
    public TicketResponseDto validateWalletBalance(Long walletId, Route route) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        if (wallet == null || wallet.getStatus().equals(ICustomerConstants.CUSTOMER_DEACTIVATED)) {
            return new TicketResponseDto(
                    getTime(), 404, "Not Found",
                    "Wallet not found given input data walletId: " + walletId,null, null, null);
        }
        if (wallet.getBalance() < route.getPrice()) {
            return new TicketResponseDto(
                    getTime(), 400, "Bad Request",
                    "Insufficient balance", null, null, null);
        }

        TicketResponseDto bt = pressingPayment(-route.getPrice(), wallet.getWalletId());

        if (bt == null) {
            return new TicketResponseDto(
                    getTime(), 200, "0k",
                    "Sufficient balance", wallet.getCustomerId(),null, null);
        }
        return bt;
    }


    /**
     * @param amount   - ticket price
     * @param walletId - wallet ID
     * @return
     */
    @Override
     public TicketResponseDto pressingPayment(double amount, long walletId){
        WalletTransaction wt = new WalletTransaction();
        wt.setAmount(amount);
        wt.setWalletId(walletId);
        boolean success = walletService.updateWallet(wt);
        if(!success){
            if(amount < 0) {
                return new TicketResponseDto(
                        getTime(), 417, "Fail",
                        "Payment processing fail", null, null, null);
            }
            return new TicketResponseDto(
                    getTime(), 417, "Fail",
                    "Refund processing fail", null, null, null);
        }
        return null;
     }

    /**
     * @param customerId - customer ID
     * @param route      - will hold route information
     * @return
     */
    @Override
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

}
