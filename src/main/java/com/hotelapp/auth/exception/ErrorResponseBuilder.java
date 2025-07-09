package com.hotelapp.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseBuilder {

    public static ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);

        Map<String, Object> errors = new HashMap<>();
        errors.put("error", message);

        body.put("errors", errors);
        return ResponseEntity.status(status).body(body);
    }

    public static ResponseEntity<Map<String, Object>> build(HttpStatus status, String message, Map<String, Object> errors) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("errors", errors);
        return ResponseEntity.status(status).body(body);
    }
}

