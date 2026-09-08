package com.proyecto.integrador.model.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "{message.required}")
    private String usuario;

    @NotBlank(message = "{message.required}")
    private String contrasena;
}