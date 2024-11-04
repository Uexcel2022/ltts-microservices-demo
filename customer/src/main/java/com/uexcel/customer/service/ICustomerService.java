package com.uexcel.customer.service;

import com.uexcel.customer.dto.CustomerDto;

public interface ICustomerService {

    /**
     * @param customerDto  - holds customer details for registration
     */
    void createCustomer(CustomerDto customerDto);

    /**
     * @param emailOrPhone  - customer email or phone number
     * @return - return customer info in customerDto objet
     */
    CustomerDto getCustomer(String emailOrPhone);

    /**
     * @param customerDto - holding updated information
     * @return - Returns boolean value indicating update successful or not
     */
    boolean updateCustomer(CustomerDto customerDto);

    /**
     * @param emailOrPhone - customer email or phone
     * @return - Returns boolean value indicating update successful or not
     */
    public boolean deleteCustomer(String emailOrPhone);

}
