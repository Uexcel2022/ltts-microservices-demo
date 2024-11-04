package com.uexcel.customer.exception;

import com.uexcel.customer.dto.CustomerErrorResponseDto;
import com.uexcel.customer.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException
            (final  ResourceNotFoundException e, final WebRequest webRequest) {
      ErrorResponseDto err =  new ErrorResponseDto(
              getTimeStamp(), 404,e.getMessage(),
                webRequest.getDescription(false));
      return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            final BadRequestException e, final WebRequest webRequest){
        ErrorResponseDto err =  new ErrorResponseDto(
                getTimeStamp(), 400,e.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<CustomerErrorResponseDto> handleInvalidInputException(
            final InvalidInputException e, final WebRequest webRequest){
        String msg = e.getUsed().size()>1? "Fields value has been used by customer.":
                "Field value has been been use by a customer";
        CustomerErrorResponseDto err =
                new CustomerErrorResponseDto(getTimeStamp(), 226,
                        HttpStatus.IM_USED,msg,
                        e.getUsed() , webRequest.getDescription(false)
                );

        return new ResponseEntity<>(err, HttpStatus.IM_USED);
    }

    @ExceptionHandler(ExceptionFail.class)
    public ResponseEntity<ErrorResponseDto> handleExceptionFail(
            final ExceptionFail e, final WebRequest webRequest){
        ErrorResponseDto err =  new ErrorResponseDto(
                getTimeStamp(), 417,e.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(final Exception e, final WebRequest webRequest) {
        ErrorResponseDto err =  new ErrorResponseDto(
                getTimeStamp(), 500,e.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        return sdf.format(new Date());
    }

}
