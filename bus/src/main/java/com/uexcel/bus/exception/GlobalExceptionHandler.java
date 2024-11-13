package com.uexcel.bus.exception;

import com.uexcel.bus.dto.BusErrorResponseDto;
import com.uexcel.bus.dto.ErrorResponseDto;
import com.uexcel.bus.dto.InvalidBusRouteIdErrorResponseDto;
import com.uexcel.bus.dto.RouteErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidInputException(
            final InvalidInputException e, WebRequest request) {
        ErrorResponseDto err =  new ErrorResponseDto(getTimestamp(new Date()),
                400, HttpStatus.BAD_REQUEST, e.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RouteException.class)
    public ResponseEntity<RouteErrorResponseDto> handleRouteException(
            final RouteException e, WebRequest request) {
        String msg =  e.getRoutes().size() > 1?
                "The following routes are in the data base or has null or empty value : ":
                "The route is in the data base or has null or empty value: ";
        RouteErrorResponseDto err =  new RouteErrorResponseDto(getTimestamp(new Date()),
                417, HttpStatus.EXPECTATION_FAILED, msg, e.getRoutes(),
                request.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(ResourceNoFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNoFoundException(
            final ResourceNoFoundException e, WebRequest request) {
        ErrorResponseDto err =  new ErrorResponseDto(getTimestamp(new Date()),
                404, HttpStatus.NOT_FOUND, e.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ExceptionFail.class)
    public ResponseEntity<ErrorResponseDto> handleExceptionFail(
            final ExceptionFail e, WebRequest request) {
        ErrorResponseDto err =  new ErrorResponseDto(getTimestamp(new Date()),
                417, HttpStatus.EXPECTATION_FAILED, e.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(err, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(InvalidRouteBusIdException.class)
    public ResponseEntity<InvalidBusRouteIdErrorResponseDto> handleInvalidRouteBusId(
            final InvalidRouteBusIdException e, WebRequest request) {
        String msg =  e.getInvalidRouteOrBusIds().size() > 1?
                "The following route ID(s) or Bus code(s) are invalid: ":"The route ID or Bus code is invalid: ";
        InvalidBusRouteIdErrorResponseDto err =  new InvalidBusRouteIdErrorResponseDto(getTimestamp(new Date()),
                400, HttpStatus.BAD_REQUEST, msg, e.getInvalidRouteOrBusIds(),
                request.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusException.class)
    public ResponseEntity<BusErrorResponseDto> handleRouteException(
            final BusException e, WebRequest request) {
        String msg =  e.getBuses().size() > 1?
                "The following buses data failed validation - capacity <= 0, bus name is null or empty: " +
                        " ":"The bus data failed validation - capacity <= 0, bus name is null or empty: ";
        BusErrorResponseDto err =  new BusErrorResponseDto(getTimestamp(new Date()),
                417, HttpStatus.EXPECTATION_FAILED, msg, e.getBuses(),
                request.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.EXPECTATION_FAILED);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(
            final Exception e, WebRequest request) {
        ErrorResponseDto err =  new ErrorResponseDto(getTimestamp(new Date()),
                500, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    public String getTimestamp(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSSZ");
            return formatter.format(date);
        }
    }







