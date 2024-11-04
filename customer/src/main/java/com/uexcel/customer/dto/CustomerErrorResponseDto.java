package com.uexcel.customer.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerErrorResponseDto {
    private String timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private List<Map<String,String>> used;
    private String apiPath;
}
