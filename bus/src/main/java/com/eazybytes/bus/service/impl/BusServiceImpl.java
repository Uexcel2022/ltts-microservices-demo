package com.eazybytes.bus.service.impl;

import com.eazybytes.bus.dto.ResponseDto;
import com.eazybytes.bus.entity.Bus;
import com.eazybytes.bus.exception.BusException;
import com.eazybytes.bus.exception.ResourceNoFoundException;
import com.eazybytes.bus.repository.BusRepository;
import com.eazybytes.bus.service.IBusService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BusServiceImpl implements IBusService {
    private final BusRepository busRepository;
    /**
     * @param bus - list of buses
     * @return  - status and message
     */
    @Override
    @Transactional
    public ResponseDto createBus(List<Bus> bus) {
        List<Bus> fail = new ArrayList<>();

        bus.forEach(el->{
            if(!validateBus(el)){
                fail.add(el);
            }
        });

        if(!fail.isEmpty()){
           throw new BusException(fail);
        }
        bus.forEach(el->{
            if(validateBus(el)){
                // Generating bus ID
                String id = busRepository.findMaxId();
                if(id==null){
                    el.setBusId("busE101");
                }else {
                    long max = Long.valueOf(id);
                    el.setBusId("busE"+(100+(max+1)));
                }
                busRepository.save(el);
            }else {
                fail.add(el);
            }
        });
        return new ResponseDto(201, SUCCESS);
    }

    /**
     * @return list of buses
     */
    @Override
    public List<Bus> fetchAllBus() {
        return busRepository.findAll();
    }

    /**
     * @param busId - bus ID
     * @return - bus
     */
    @Override
    public Bus fetchBusById(String busId) {
        return busRepository.findByBusId(busId)
                .orElseThrow(()-> new ResourceNoFoundException("Bus","busId",busId)
        );
    }


}
