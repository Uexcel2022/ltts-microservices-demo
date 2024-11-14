package com.uexcel.customer.controller;

import com.uexcel.customer.dto.ResponseDto;
import com.uexcel.customer.dto.WalletDto;
import com.uexcel.customer.dto.FundTransferDto;
import com.uexcel.customer.entity.WalletTransaction;
import com.uexcel.customer.exception.ExceptionFail;
import com.uexcel.customer.service.IWalletService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("http://localhost:8080")
public class WalletController {
    private final IWalletService iWalletService;
    private final Logger logger = LoggerFactory.getLogger(WalletController.class.getName());
    @GetMapping("/wallet")
    public ResponseEntity<WalletDto> fetchWallet(@RequestParam long walletId,
                                                 @RequestHeader("saferideCorrelationId") String correlationId) {
        WalletDto walletDto = iWalletService.fetchWallet(walletId);
        logger.debug("fetching wallet: saferideCorrelation-id found: {}", correlationId);
        return ResponseEntity.ok(walletDto);
    }

    @PostMapping("/wallet")
    public ResponseEntity<ResponseDto> fund(@RequestBody WalletTransaction wt) {
        iWalletService.fundWallet(wt);
        return ResponseEntity.ok()
                .body(new ResponseDto(201,"Wallet funded successfully."));
    }

    @PutMapping("/wallet")
    public ResponseEntity<Boolean> update(@RequestBody WalletTransaction wt,
                                          @RequestHeader("saferideCorrelationId") String correlationId) {
      boolean success =  iWalletService.updateWallet(wt);
        if(success){
            logger.debug("Updating wallet: success: saferideCorrelation-id found {}", correlationId);
            return ResponseEntity.ok(true);
        }
        logger.debug("Updating wallet: fail: saferideCorrelation-id found {}", correlationId);
        throw  new ExceptionFail("Fail");
    }

    @PutMapping("/transfer")
    public ResponseEntity<ResponseDto> transfer(@RequestBody FundTransferDto ft) {
        iWalletService.walletTransfer(ft);
        return ResponseEntity.ok()
                .body(new ResponseDto(200,"fund transferred successfully."));
    }
}
