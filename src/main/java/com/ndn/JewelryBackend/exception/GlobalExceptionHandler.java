package com.ndn.JewelryBackend.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndn.JewelryBackend.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) throws
            JsonProcessingException {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String jsonErrors = new ObjectMapper().writeValueAsString(errors);
        ApiResponse apiResponse =  ApiResponse.builder()
                .status(false)
                .code(400)
                .data(null)
                .message(jsonErrors)
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
        ApiResponse apiResponse =  ApiResponse.builder()
                .status(false)
                .code(400)
                .data(null)
                .message(ex.getMessage())
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> handleInsufficientStockException(InsufficientStockException ex, WebRequest request){
        ApiResponse apiResponse =  ApiResponse.builder()
                .status(false)
                .code(500)
                .data(null)
                .message(ex.getMessage())
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request){
        ApiResponse apiResponse =  ApiResponse.builder()
                .status(false)
                .code(500)
                .data(null)
                .message(ex.getMessage())
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}