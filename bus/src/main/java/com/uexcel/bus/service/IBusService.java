package com.uexcel.bus.service;

import com.uexcel.bus.dto.ResponseDto;
import com.uexcel.bus.entity.Bus;

import java.util.List;

public interface IBusService {
   String SUCCESS ="Request processed successfully";
   String error ="Request processed failed";
    /**
     * @param bus - list of buses
     * @return  - status and message
     */
    ResponseDto createBus(List<Bus> bus);

    /**
     * @return list of buses
     */
    List<Bus> fetchAllBus();

    /**
     * @param busId - bus ID
     * @return - bus
     */
    Bus fetchBusById(String busId);

    default boolean validateBus(Bus bus){
        return bus.getBusName() != null && bus.getCapacity() > 0;
    }



}
