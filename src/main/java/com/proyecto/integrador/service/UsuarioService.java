package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity<Object> registrarUsuario(RegistrarUsuarioRequest request);
}
