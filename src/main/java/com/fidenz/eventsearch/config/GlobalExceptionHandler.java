package com.fidenz.eventsearch.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception ex){
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

}