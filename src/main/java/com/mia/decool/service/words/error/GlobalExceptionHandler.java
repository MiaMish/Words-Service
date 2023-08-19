package com.mia.decool.service.words.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // You can add more exception handlers here, to handle different exceptions in different ways.

    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleOtherExceptions(Exception ex) throws Exception {
        throw ex;
    }

}
