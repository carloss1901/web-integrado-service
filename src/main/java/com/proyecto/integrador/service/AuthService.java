package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.auth.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<Object> login(LoginRequest request);
}