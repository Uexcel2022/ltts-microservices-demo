package com.uexcel.ticketing.repositorty;

import com.uexcel.ticketing.entity.BusRoute;
import com.uexcel.ticketing.entity.BusRouteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, BusRouteId> {
    List<BusRoute> findBusRouteByBusId(String busId);
    List<BusRoute> findBusRouteByRouteId(long routeId);
    boolean existsBusRouteByBusIdAndRouteId(String busId, long routeId);
}