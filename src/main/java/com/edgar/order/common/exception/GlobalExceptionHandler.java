package com.edgar.order.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 🔴 Not Found
    @ExceptionHandler({
            ProductNotFoundException.class,
            CustomerNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        return buildResponse(ex, HttpStatus.NOT_FOUND);
    }

    // 🔴 Bad Request
    @ExceptionHandler({
            InsufficientStockException.class,
            InvalidOrderStateException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }

    // 🔴 Validaciones (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error");

        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    // 🔴 Catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 🔧 Builder centralizado
    private ResponseEntity<ErrorResponse> buildResponse(Exception ex, HttpStatus status) {
        return buildResponse(ex.getMessage(), status);
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();

        return new ResponseEntity<>(error, status);
    }
}