package com.uexcel.customer.controller;

import com.uexcel.customer.constants.ICustomerConstants;
import com.uexcel.customer.dto.CustomerDto;
import com.uexcel.customer.dto.ResponseDto;
import com.uexcel.customer.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CustomerController {
    private final ICustomerService iCustomerService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> getAllCustomers(@RequestBody CustomerDto customerDto) {
        iCustomerService.createCustomer(customerDto);
        return ResponseEntity.ok().body(
                new ResponseDto(201,ICustomerConstants.MSG_201)
        );
    }
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchCustomers(@RequestParam String emailOrPhone) {
        return ResponseEntity.ok().body(iCustomerService.getCustomer(emailOrPhone));
    }
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCustomer(
            @RequestBody CustomerDto customerDto) {
        boolean status = iCustomerService.updateCustomer(customerDto);
        if (status) {
            return ResponseEntity.ok().body(
                    new ResponseDto(200,ICustomerConstants.MSG_200_UPDATE));
        }
        return ResponseEntity.ok().body(
                new ResponseDto(417,
                        ICustomerConstants.MSG_417));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCustomer(
            @RequestParam String emailOrPhone) {
        boolean status = iCustomerService.deleteCustomer(emailOrPhone);
        if (status) {
            return ResponseEntity.ok().body(
                    new ResponseDto(200,ICustomerConstants.MSG_200_DELETE));
        }
        return ResponseEntity.ok().body(
                new ResponseDto(417,
                        ICustomerConstants.MSG_417));
    }
}
