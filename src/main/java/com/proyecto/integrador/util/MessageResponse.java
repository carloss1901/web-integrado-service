package com.proyecto.integrador.util;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class MessageResponse {

    private MessageResponse() {
    }

    public static ResponseEntity<Object> setResponse(
            boolean success,
            HttpStatus status,
            @Nullable String message,
            @Nullable Object data
    ) {
        Map<String, Object> response = crearRespuesta(
                success,
                status,
                message,
                data
        );

        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Object> setResponse(
            boolean success,
            HttpStatus status,
            String message
    ) {
        return setResponse(
                success,
                status,
                message,
                null
        );
    }

    public static ResponseEntity<Object> setResponse(
            boolean success,
            String message
    ) {
        HttpStatus status = success
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return setResponse(
                success,
                status,
                message,
                null
        );
    }

    public static ResponseEntity<Object> setResponse(
            Set<? extends ConstraintViolation<?>> violations
    ) {
        String mensaje = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return setResponse(
                false,
                HttpStatus.BAD_REQUEST,
                mensaje,
                null
        );
    }

    private static Map<String, Object> crearRespuesta(
            boolean success,
            HttpStatus status,
            @Nullable String message,
            @Nullable Object data
    ) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("success", success);
        response.put("message", message);
        response.put("status", status.value());
        response.put("data", data);

        return response;
    }
}