package com.eazybytes.ticketing.controller;

import com.eazybytes.ticketing.dto.CheckinDto;
import com.eazybytes.ticketing.dto.ResponseDto;
import com.eazybytes.ticketing.service.impl.CheckinServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CheckinController {

    private final CheckinServiceImpl checkinService;

    @PostMapping("/checkin")
    public ResponseEntity<ResponseDto> checkInByTicketNumber(@RequestBody CheckinDto checkinDto){
        return ResponseEntity.ok().body(checkinService.checkinValidation(checkinDto));
    }

}
