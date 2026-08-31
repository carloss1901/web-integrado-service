package com.proyecto.integrador.util;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageResponse {

    public MessageResponse() {
    }

    public static ResponseEntity<Object> setResponse(@Nullable boolean success, HttpStatus status, @Nullable String message, @Nullable Object data) {
        Map<String, Object> map = new HashMap();
        map.put("success", success);
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", data);
        return new ResponseEntity(map, status);
    }

    public static ResponseEntity<Object> setResponse(boolean success, HttpStatus status, String message) {
        Map<String, Object> map = new HashMap();
        map.put("success", success);
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", (Object)null);
        return new ResponseEntity(map, status);
    }

    public static ResponseEntity<Object> setResponse(boolean success, String message) {
        HttpStatus status = success ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        Map<String, Object> map = new HashMap();
        map.put("success", success);
        map.put("message", message);
        map.put("status", status);
        map.put("data", (Object)null);
        return new ResponseEntity(map, status);
    }

    public static ResponseEntity<Object> setResponse(Set<? extends ConstraintViolation<?>> violations) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> map = new HashMap();
        map.put("success", false);
        map.put("message", violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
        map.put("status", status);
        map.put("data", (Object)null);
        return new ResponseEntity(map, status);
    }
}
