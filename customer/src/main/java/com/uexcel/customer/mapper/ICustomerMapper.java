package com.uexcel.customer.mapper;

import com.uexcel.customer.constants.ICustomerConstants;
import com.uexcel.customer.dto.CustomerDto;
import com.uexcel.customer.dto.WalletDto;
import com.uexcel.customer.entity.Customer;
import com.uexcel.customer.entity.Wallet;

import java.util.Random;

public interface ICustomerMapper {

    default Customer mapToNewCustomer(CustomerDto customerDto ) {
        Customer customer = new Customer();
        customer.setEmailAddress(customerDto.getEmailAddress());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(customerDto.getPassword());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setStatus(ICustomerConstants.CUSTOMER_ACTIVE);
        return customer;
    }

    default CustomerDto mapToCustomerDto(Customer customer, Wallet wallet) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setEmailAddress(customer.getEmailAddress());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        wallet.setCustomerId(customer.getCustomerId());
        customerDto.setWalletDto(mapToWalletDto(wallet));
        customerDto.setCustomerId(customer.getCustomerId());
        return customerDto;
    }

    default Customer mapToUpdateCustomer(CustomerDto customerDto, Customer customer ) {
        customer.setEmailAddress(customerDto.getEmailAddress());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        return customer;
    }

    default Wallet createWallet(String customerId) {
        Long walletId = 1000000000000L + new  Random().nextInt(900000000);
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setCustomerId(customerId);
        wallet.setStatus(ICustomerConstants.CUSTOMER_ACTIVE);
        wallet.setBalance(1500.00);
        return wallet;
    }

    default WalletDto mapToWalletDto(Wallet wallet) {
        WalletDto walletDto = new WalletDto();
        walletDto.setWalletId(wallet.getWalletId());
        walletDto.setBalance(wallet.getBalance());
        walletDto.setCustomerId(wallet.getCustomerId());
        return walletDto;
    }

}
