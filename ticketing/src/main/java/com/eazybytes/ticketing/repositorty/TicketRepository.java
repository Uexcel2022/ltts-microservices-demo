package com.eazybytes.ticketing.repositorty;

import com.eazybytes.ticketing.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    Ticket findByRoutIdAndCustomerIdAndStatusIgnoreCase(
            long routeId, String customerId, String status);
    List<Ticket> findByCustomerIdAndStatusIgnoreCase(String customerId, String status);

}
