package com.uexcel.customer.service;

import com.uexcel.customer.dto.FundTransferDto;
import com.uexcel.customer.dto.WalletDto;
import com.uexcel.customer.entity.WalletTransaction;

public interface IWalletService {
    String ACCT_DS = "wallet", T_TYPE_TICKET ="ticket",T_TYPE_TRANSFER ="transfer";
    /**
     * @param walletId - the customer wallet id
     * @return WalletDto info
     */
    WalletDto fetchWallet(long walletId);

    /**
     * @param wt  - request body - wallet transaction
     * @return boolean value indicating update is successful or not
     */
    boolean updateWallet(WalletTransaction wt);

    /**
     * @param wt  - request body - wallet transaction
     * @return boolean value indicating wallet funding is successful or not
     */
    boolean fundWallet(WalletTransaction wt);


    /**
     * @param ft  - hold the two wallet IDs in involve
     */
    void walletTransfer(FundTransferDto ft);

}
