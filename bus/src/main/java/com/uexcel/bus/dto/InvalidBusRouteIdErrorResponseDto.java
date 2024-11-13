package com.uexcel.bus.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class InvalidBusRouteIdErrorResponseDto {
    private String timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private List<String> busCodeRouteId;
    private String apiPath;
}
