package org.vromanyu.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class UserBalanceControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex, HttpServletRequest request) {
        Map<String, Object> response = Map.of(
                "path", request.getRequestURI(),
                "error", ex.getMessage(),
                "timestamp", OffsetDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.getReasonPhrase()
        );
        return ResponseEntity.badRequest().body(response);
    }
}
