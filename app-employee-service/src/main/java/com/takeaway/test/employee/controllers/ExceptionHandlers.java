package com.takeaway.test.employee.controllers;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.common.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * weisspos
 * <p>
 * ???
 *
 * @author Pavel
 * @since 06/03/2018
 */

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<String> persistenceExceptionHandler(PersistenceException e, WebRequest request) {
        log.error("PersistenceException, Request {}, Message {}", request.getDescription(true), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error occured while calling DB");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> persistenceExceptionHandler(ResourceNotFoundException e, WebRequest request) {
        log.error("ResourceNotFoundException, Request {}, Message {}", request.getDescription(true), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Resource not found");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, WebRequest request) {
        log.error("MethodArgumentNotValidException, Request {}, Message {}", request.getDescription(true), e.getMessage());
        return ResponseEntity
                .badRequest()
                .body("Input arguments validation failed");
    }

}
