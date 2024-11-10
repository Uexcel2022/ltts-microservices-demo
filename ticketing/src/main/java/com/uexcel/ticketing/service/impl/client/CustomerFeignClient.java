package com.uexcel.ticketing.service.impl.client;

import com.uexcel.ticketing.dto.WalletDto;
import com.uexcel.ticketing.dto.WalletTransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "customer",fallback = CustomerFeignClientFallback.class)
public interface CustomerFeignClient {
    @GetMapping("/api/wallet")
    ResponseEntity<WalletDto> fetchWallet(@RequestParam long walletId,
                                          @RequestHeader("saferideCorrelationId") String correlationId);

    @PutMapping("/api/wallet")
    ResponseEntity<Boolean> updateWallet(@RequestBody WalletTransactionDto wt,
                                         @RequestHeader("saferideCorrelationId") String correlationId);
}