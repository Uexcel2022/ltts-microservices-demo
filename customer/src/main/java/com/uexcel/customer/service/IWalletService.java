package com.uexcel.customer.service;

import com.uexcel.customer.dto.WalletDto;
import com.uexcel.customer.entity.WalletTransaction;

public interface IWalletService {
    String ACCT_DS = "wallet", T_TYPE ="ticket";
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


}
