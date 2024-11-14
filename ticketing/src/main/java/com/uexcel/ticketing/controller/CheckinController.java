package com.uexcel.ticketing.controller;

import com.uexcel.ticketing.dto.CheckinDto;
import com.uexcel.ticketing.dto.ResponseDto;
import com.uexcel.ticketing.service.impl.CheckinServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CheckinController {

    private final CheckinServiceImpl checkinService;
    private final Logger logger = LoggerFactory.getLogger(CheckinController.class);


    @PostMapping("/checkin")
    public ResponseEntity<ResponseDto> checkin(@RequestBody CheckinDto checkinDto,
                                               @RequestHeader("saferideCorrelationId") String correlationId){
        logger.debug("checkin: saferideCorrelation-id {}", correlationId);
        return ResponseEntity.ok().body(checkinService.checkinValidation(checkinDto,correlationId));
    }

}
