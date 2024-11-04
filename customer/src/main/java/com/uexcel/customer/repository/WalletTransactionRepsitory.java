package com.uexcel.customer.repository;

import com.uexcel.customer.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletTransactionRepsitory extends JpaRepository<WalletTransaction, Long> {

}
