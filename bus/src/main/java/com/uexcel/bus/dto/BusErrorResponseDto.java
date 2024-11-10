package com.uexcel.bus.dto;

import com.uexcel.bus.entity.Bus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class BusErrorResponseDto {
    private String timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private List<Bus> bus;
    private String apiPath;
}
