package com.eazybytes.bus.controller;

import com.eazybytes.bus.service.IBusService;
import com.eazybytes.bus.dto.ResponseDto;
import com.eazybytes.bus.entity.Bus;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
public class BusController {

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
    public ResponseEntity<Bus> fetchBus(@RequestParam String busId){
        return ResponseEntity.ok()
                .body(ibusService.fetchBusById(busId));
    }
}
