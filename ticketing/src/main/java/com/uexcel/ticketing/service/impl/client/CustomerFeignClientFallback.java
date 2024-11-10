package com.uexcel.ticketing.service.impl.client;

import com.uexcel.ticketing.dto.WalletDto;
import com.uexcel.ticketing.dto.WalletTransactionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerFeignClientFallback implements CustomerFeignClient {
    @Override
    public ResponseEntity<WalletDto> fetchWallet(long walletId, String correlationId) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> updateWallet(WalletTransactionDto wt, String correlationId) {
        return null;
    }
}
