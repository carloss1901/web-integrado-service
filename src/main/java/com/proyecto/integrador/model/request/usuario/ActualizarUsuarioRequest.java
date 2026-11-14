package com.proyecto.integrador.model.request.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActualizarUsuarioRequest {
    @NotNull(message = "{message.required}")
    private Integer idUsuario;

    @NotBlank(message = "{message.required}")
    private String nombres;

    @NotBlank(message = "{message.required}")
    @NotBlank(message = "{message.required}")
    private String apePaterno;

    @Size(max = 100, message = "El apellido materno no puede superar los 100 caracteres.")
    private String apeMaterno;

    @NotBlank(message = "{message.required}")
    @Email(message = "{message.email}")
    private String correo;

    @NotNull(message = "{message.required}")
    private List<Integer> idsRoles;
}
