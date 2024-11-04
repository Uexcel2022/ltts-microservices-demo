package com.eazybytes.ticketing.repositorty;

import com.eazybytes.ticketing.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
    Route findByRouteId(Long name);
    Route findByOriginAndDestination(String origin, String destination);
}
