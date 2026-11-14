package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.auth.LoginRequest;
import com.proyecto.integrador.model.request.auth.SeleccionarRolRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<Object> login(LoginRequest request);
    ResponseEntity<Object> seleccionarRol(SeleccionarRolRequest request, String token);
}
