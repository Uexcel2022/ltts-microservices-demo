package com.uexcel.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class ResponseDto {
    private int code;
    private String message;
}
