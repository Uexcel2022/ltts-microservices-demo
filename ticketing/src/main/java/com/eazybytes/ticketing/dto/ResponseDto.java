package com.eazybytes.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto {
    private int status;
    private String message;
}
