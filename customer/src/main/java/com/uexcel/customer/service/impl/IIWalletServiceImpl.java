package com.uexcel.customer.service.impl;

import com.uexcel.customer.constants.ICustomerConstants;
import com.uexcel.customer.dto.FundTransferDto;
import com.uexcel.customer.dto.WalletDto;
import com.uexcel.customer.entity.Wallet;
import com.uexcel.customer.entity.WalletTransaction;
import com.uexcel.customer.exception.BadRequestException;
import com.uexcel.customer.exception.ResourceNotFoundException;
import com.uexcel.customer.mapper.ICustomerMapper;
import com.uexcel.customer.repository.WalletRepository;
import com.uexcel.customer.repository.WalletTransactionRepsitory;
import com.uexcel.customer.service.IWalletService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@AllArgsConstructor
public class IIWalletServiceImpl implements IWalletService {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepsitory wTransactionRepository;
    private final ICustomerMapper iCustomerMapper;
    /**
     * @param walletId - the customer wallet id
     * @return Wallet info
     */
    @Override
    public WalletDto fetchWallet(long walletId) {
        return iCustomerMapper.mapToWalletDto(getWallet(walletId));
    }

    /**
     * @param wt - request body - wallet transaction
     * @return boolean value indicating update is successful or not
     */
    @Override
    @Transactional
    public boolean updateWallet(WalletTransaction wt) {
        Wallet wallet = getWallet(wt.getWalletId());
        double newBal = wt.getAmount() + wallet.getBalance();
          wallet.setBalance(newBal);
          WalletTransaction walletTransaction = new WalletTransaction();
          walletTransaction.setAmount(wt.getAmount());
          walletTransaction.setWalletId(wt.getWalletId());
          walletTransaction.setAccountNumber(Long.toString(wt.getWalletId()));
          walletTransaction.setTransactionType(T_TYPE_TICKET);
          walletTransaction.setAccountDescription(ACCT_DS);
          walletTransaction.setDate(new Date().toString());
          walletRepository.save(wallet);
          wTransactionRepository.save(walletTransaction);
        return true;
    }

    /**
     * @param wt - request body - wallet transaction
     * @return boolean value indicating wallet funding is successful or not
     */
    @Override
    @Transactional
    public boolean fundWallet(WalletTransaction wt) {
        if(wt.getAmount() <= 0){
            throw new BadRequestException("Amount must be greater than 0: "+wt.getAmount());
        }
       Wallet wallet = getWallet(wt.getWalletId());
       wallet.setBalance(wallet.getBalance() + wt.getAmount());
       wt.setDate(LocalDate.now().toString());
       wTransactionRepository.save(wt);
       walletRepository.save(wallet);
        return true;
    }

    /**
     * @param ft - hold the two wallet IDs in involve
     */
    @Override
    @Transactional
    public void walletTransfer(FundTransferDto ft) {
        if(ft.getAmount() <= 0){
            throw new BadRequestException("Amount must be greater than 0:" +
                    " "+ft.getAmount());
        }
        Wallet payer = getWallet(ft.getPayerWalletId());
        payer.setBalance(payer.getBalance() - ft.getAmount());
        WalletTransaction wPayer = new WalletTransaction();
        wPayer.setAmount(-ft.getAmount());
        wPayer.setAccountNumber(String.valueOf(ft.getPayerWalletId()));
        wTransactionRepository.save(getWalletTransaction(ft.getPayerWalletId(),wPayer));
        walletRepository.save(payer);

        Wallet payee = getWallet(ft.getPayeeWalletId());
        payee.setBalance(payee.getBalance() + ft.getAmount());
        WalletTransaction wPayee = new WalletTransaction();
        wPayee.setAmount(ft.getAmount());
        wPayee.setAccountNumber(String.valueOf(ft.getPayerWalletId()));
        wTransactionRepository.save(getWalletTransaction(ft.getPayeeWalletId(),wPayee));
        walletRepository.save(payee);

    }

    private Wallet getWallet(long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(()-> new ResourceNotFoundException(
                                "Wallet","walletId",Long.toString(walletId)
                        )
                );
        if (ICustomerConstants.CUSTOMER_DEACTIVATED.equals(wallet.getStatus())){
            throw new BadRequestException("Customer is deactivated");
        }
        return wallet;
    }

    private WalletTransaction getWalletTransaction(long walletId, WalletTransaction w) {
        w.setDate(LocalDate.now().toString());
        w.setAccountDescription(ACCT_DS);
        w.setTransactionType(T_TYPE_TRANSFER);
        w.setWalletId(walletId);
        return w;
    }
}
