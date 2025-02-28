package com.hotelapp.auth.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getMessage().contains("users.email")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bu email adresi zaten kayıtlı. Lütfen farklı bir email adresi kullanın.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Veri bütünlüğü hatası: " + ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hata: " + ex.getMessage());
    }
}
