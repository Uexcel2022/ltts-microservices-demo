package com.uexcel.ticketing.repositorty;

import com.uexcel.ticketing.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
    Route findByRouteId(Long name);
    Route findByOriginAndDestination(String origin, String destination);
}
