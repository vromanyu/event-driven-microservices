package org.vromanyu.write.product;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class ProductControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ProductControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex, HttpServletRequest request) {
        logger.error("exception occurred", ex);
        Map<String, Object> response = Map.of(
                "path", request.getRequestURI(),
                "error", ex.getMessage(),
                "timestamp", OffsetDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.getReasonPhrase()
        );
        return ResponseEntity.badRequest().body(response);
    }
}
