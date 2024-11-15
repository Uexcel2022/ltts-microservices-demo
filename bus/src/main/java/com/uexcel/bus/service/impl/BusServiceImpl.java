package com.uexcel.bus.service.impl;

import com.uexcel.bus.dto.ResponseDto;
import com.uexcel.bus.entity.Bus;
import com.uexcel.bus.exception.BusException;
import com.uexcel.bus.exception.ResourceNoFoundException;
import com.uexcel.bus.repository.BusRepository;
import com.uexcel.bus.service.IBusService;
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
                    el.setBusCode("busE101");
                }else {
                    long max = Long.valueOf(id);
                    el.setBusCode("busE"+(100+(max+1)));
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
        return busRepository.findByBusCode(busId)
                .orElseThrow(()-> new ResourceNoFoundException("Bus","busId",busId)
        );
    }


}
