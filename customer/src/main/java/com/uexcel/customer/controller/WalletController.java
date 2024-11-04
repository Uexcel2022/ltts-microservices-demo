package com.uexcel.customer.controller;

import com.uexcel.customer.dto.ResponseDto;
import com.uexcel.customer.dto.WalletDto;
import com.uexcel.customer.entity.WalletTransaction;
import com.uexcel.customer.exception.ExceptionFail;
import com.uexcel.customer.service.IWalletService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("http://localhost:8080")
public class WalletController {
    private final IWalletService iWalletService;
    @GetMapping("/wallet")
    public ResponseEntity<WalletDto> fetchWallet(@RequestParam long walletId) {
        return ResponseEntity.ok(iWalletService.fetchWallet(walletId));
    }

    @PostMapping("/wallet")
    public ResponseEntity<ResponseDto> fund(@RequestBody WalletTransaction wt) {
        iWalletService.fundWallet(wt);
        return ResponseEntity.ok()
                .body(new ResponseDto("201","Wallet founded successfully."));
    }

    @PutMapping("/wallet")
    public ResponseEntity<Boolean> update(@RequestBody WalletTransaction wt) {
      boolean success =  iWalletService.updateWallet(wt);
        if(success){
            return ResponseEntity.ok(true);
        }
        throw  new ExceptionFail("Fail");
    }
}
