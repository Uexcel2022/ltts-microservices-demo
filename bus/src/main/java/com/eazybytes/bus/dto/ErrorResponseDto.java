package com.eazybytes.bus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorResponseDto {
    private String timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String apiPath;
}
