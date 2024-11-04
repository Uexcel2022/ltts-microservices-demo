package com.uexcel.customer.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.util.List;
import java.util.Map;


@ResponseStatus(HttpStatus.IM_USED)
@Getter @Setter
@NoArgsConstructor
public class InvalidInputException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Map<String,String>> used;
    public InvalidInputException(List<Map<String,String>> used) {
        this.used = used;
    }

}
