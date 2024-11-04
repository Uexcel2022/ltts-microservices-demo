package com.uexcel.customer.service.impl;

import com.uexcel.customer.constants.ICustomerConstants;
import com.uexcel.customer.dto.CustomerDto;

import com.uexcel.customer.entity.Customer;
import com.uexcel.customer.entity.Wallet;
import com.uexcel.customer.exception.BadRequestException;
import com.uexcel.customer.exception.InvalidInputException;
import com.uexcel.customer.exception.ResourceNotFoundException;
import com.uexcel.customer.mapper.ICustomerMapper;
import com.uexcel.customer.repository.CustomerRepository;
import com.uexcel.customer.repository.WalletRepository;
import com.uexcel.customer.service.ICustomerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {
    private final ICustomerMapper iCustomerMapper;
    private  final CustomerRepository customerRepository;
    private  final WalletRepository welledRepository;

    /**
     * @param customerDto - holds customer details for registration
     */
    @Override
    @Transactional
    public void createCustomer(CustomerDto customerDto) {

        List<Map<String,String>> used = new ArrayList<>();
        Map<String,String> map1 = new HashMap<>();
        Map<String,String> map2 = new HashMap<>();
        boolean existsEmail = customerRepository
                .existsByEmailAddress(customerDto.getEmailAddress());
        boolean existsPhoneNumber = customerRepository
                .existsByPhoneNumber(customerDto.getPhoneNumber());

        if (existsEmail) {
           map1.put("emailAddress",
                   customerDto.getEmailAddress());
           used.add(map1);
        }

        if (existsPhoneNumber) {
            map2.put("phoneNumber",customerDto.getPhoneNumber());
            used.add(map2);
        }

        if(!used.isEmpty()){
            throw new  InvalidInputException(used);
        }

        Customer customer =
                customerRepository.save(iCustomerMapper
                        .mapToNewCustomer(customerDto));
        Wallet  wallet = iCustomerMapper.createWallet(customer.getCustomerId());
        welledRepository.save(wallet);
    }

    /**
     * @param emailOrPhone - customer email or phone number
     * @return - return customer info with customerDto objet
     */
    @Override
    public CustomerDto getCustomer(String emailOrPhone) {
        Customer customer = customerExists(emailOrPhone);
        Wallet wallet = walletExists(customer.getCustomerId());
      return   iCustomerMapper.mapToCustomerDto(customer,wallet);
    }

    /**
     * @param customerDto - holding updated information
     * @return - Returns boolean value indicating update successful or not
     */
    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        Customer customer = customerExists(customerDto.getEmailAddress());
       customerRepository.save(
               iCustomerMapper.mapToUpdateCustomer(customerDto,customer));
        return true;
    }

    /**
     * @param emailOrPhone - customer email or phone
     * @return - return boolean value indicating delete is successful or not
     */
    public boolean deleteCustomer(String emailOrPhone) {
        Customer customer = customerExists(emailOrPhone);
        customer.setStatus(ICustomerConstants.CUSTOMER_DEACTIVATED);
        Wallet wallet = walletExists(customer.getCustomerId());
        wallet.setStatus(ICustomerConstants.CUSTOMER_DEACTIVATED);
        customerRepository.save(customer);
        welledRepository.save(wallet);
        return true;
    }



    private Customer customerExists(String emailOrPhone) {
        Customer customer = customerRepository
                .findByEmailAddressOrPhoneNumber(emailOrPhone,emailOrPhone)
                .orElseThrow(()->{
                    if(emailOrPhone!= null && !emailOrPhone.isEmpty()) {
                        if (emailOrPhone.contains("@")) {
                            return new ResourceNotFoundException("Customer", "emailAddress", emailOrPhone);
                        } else {
                            return new ResourceNotFoundException("Customer", "PhoneNumber", emailOrPhone);
                        }
                    } else {
                        return new BadRequestException("The inputted value is Null or Empty");
                    }
                });
        if(customer.getStatus().equals(ICustomerConstants.CUSTOMER_DEACTIVATED)) {
            throw new BadRequestException("Customer is deactivated.");
        }
        return customer;
    }


    private Wallet walletExists(String customerId) {
       return welledRepository.findByCustomerId(customerId)
                .orElseThrow(()->new ResourceNotFoundException("Wallet","CustomerId",customerId));
    }



}
