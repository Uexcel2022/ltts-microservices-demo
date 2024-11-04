package com.uexcel.customer.repository;

import com.uexcel.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {
    Optional<Customer> findByEmailAddressOrPhoneNumber(String email, String phone);
    boolean existsByEmailAddress(String emailAddress);
    boolean existsByPhoneNumber(String emailAddress);
}
