package com.uexcel.bus.repository;

import com.uexcel.bus.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findByBusCode(String busId);
    @Query(nativeQuery = true, value = "select max(id) from bus")
    String findMaxId();
}
