package com.takeaway.test.employee.controllers;

import com.takeaway.test.common.exceptions.ConstraintException;
import com.takeaway.test.common.exceptions.DuplicateResourceException;
import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.common.exceptions.ResourceNotFoundException;
import com.takeaway.test.employee.model.web.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    public ResponseEntity<ErrorResponse> persistenceExceptionHandler(PersistenceException e, WebRequest request) {
        log.error("PersistenceException, Request {}, Message {}", request.getDescription(true), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error occured while calling DB")
                    .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> persistenceExceptionHandler(ResourceNotFoundException e, WebRequest request) {
        log.error("ResourceNotFoundException, Request {}, Message {}", request.getDescription(true), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Resource not found")
                        .build());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> duplicateResourceExceptionHandler(DuplicateResourceException e, WebRequest request) {
        log.error("DuplicateResourceException, Request {}, Message {}", request.getDescription(true), e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Non unique resource provided")
                        .build());
    }

    @ExceptionHandler(ConstraintException.class)
    public ResponseEntity<ErrorResponse> constraintExceptionHandler(ConstraintException e, WebRequest request) {
        log.error("ConstraintException, Request {}, Message {}", request.getDescription(true), e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("DB constraint violated")
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, WebRequest request) {
        log.error("MethodArgumentNotValidException, Request {}, Message {}", request.getDescription(true), e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Input arguments validation failed")
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e, WebRequest request) {
        log.error("HttpMessageNotReadableException, Request {}, Message {}", request.getDescription(true), e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Failed to convert message to model")
                        .build());
    }

}
