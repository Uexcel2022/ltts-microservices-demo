package com.uexcel.ticketing.controller;

import com.uexcel.ticketing.dto.CheckinDto;
import com.uexcel.ticketing.dto.ResponseDto;
import com.uexcel.ticketing.service.impl.CheckinServiceImpl;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
//    @RateLimiter(name = "checkin",fallbackMethod = "checkinFallback")
    public ResponseEntity<ResponseDto> checkin(@RequestBody CheckinDto checkinDto){
        return ResponseEntity.ok().body(checkinService.checkinValidation(checkinDto));
    }

//    private ResponseEntity<String> checkinFallback(Throwable throwable){
//       return ResponseEntity.status(429).body("Too many requests");
//    }
}
