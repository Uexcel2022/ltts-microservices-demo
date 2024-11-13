package com.uexcel.bus.repository;


import com.uexcel.bus.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
    Route findByRouteId(Long name);
    Route findByOriginAndDestination(String origin, String destination);
    List<Route> findByRegion(String region);
}
