package com.eazybytes.ticketing.service.impl.client;

import com.eazybytes.ticketing.dto.WalletDto;
import com.eazybytes.ticketing.dto.WalletTransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("customer")
public interface CustomerFeignClient {
    @GetMapping("/api/wallet")
    ResponseEntity<WalletDto> fetchWallet(@RequestParam long walletId);

    @PutMapping("/api/wallet")
    ResponseEntity<Boolean> updateWallet(@RequestBody WalletTransactionDto wt);
}
