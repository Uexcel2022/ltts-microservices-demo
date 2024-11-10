package com.uexcel.bus.controller;

import com.uexcel.bus.service.IBusService;
import com.uexcel.bus.dto.ResponseDto;
import com.uexcel.bus.entity.Bus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
public class BusController {
private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final IBusService ibusService;
    @PostMapping("create-bus")
    public ResponseEntity<ResponseDto> createBus(@RequestBody List<Bus> buses){
        return ResponseEntity.status(201)
                .body(ibusService.createBus(buses));
    }

    @GetMapping("/fetch-all-bus")
    public ResponseEntity<List<Bus>> fetchAll(){
       return ResponseEntity.ok()
               .body(ibusService.fetchAllBus());
    }

    @GetMapping("/fetch-bus")
    public ResponseEntity<Bus> fetchBus(@RequestParam String busId,
            @RequestHeader("saferideCorrelationId") String correlationId ){
        Bus bus = ibusService.fetchBusById(busId);
        logger.debug("saferideCorrelation-id found: {}", correlationId);
        return ResponseEntity.ok().body(bus);
    }
}
