package com.proyecto.integrador.model.request.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistrarUsuarioRequest {
    @NotBlank(message = "{message.required}")
    private String usuario;

    @NotBlank(message = "{message.required}")
    private String nombres;

    @NotBlank(message = "{message.required}")
    private String apellidos;

    @NotBlank(message = "{message.required}")
    @Email(message = "{message.email}")
    private String correo;

    @NotBlank(message = "{message.required}")
    private String password;

    @NotNull(message = "{message.required}")
    private List<Integer> idsRoles;
}
