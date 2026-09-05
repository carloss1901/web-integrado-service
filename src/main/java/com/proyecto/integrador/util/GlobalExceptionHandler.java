package com.proyecto.integrador.util;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorCampo> errores = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            errores.add(new ErrorCampo(fieldError.getField(), error.getDefaultMessage()));
        });

        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST,
            "Errores de validacion", errores);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ErrorCampo> errores = ex.getConstraintViolations().stream()
            .map(error -> new ErrorCampo(error.getPropertyPath().toString(), error.getMessage()))
            .toList();

        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST,
            "Errores de validacion", errores);
    }
}
