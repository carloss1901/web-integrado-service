package com.proyecto.integrador.model.request.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistrarUsuarioRequest {
    @NotBlank(message = "{message.required}")
    @Size(max = 8, message = "El DNI no puede superar los 8 dígitos.")
    @Pattern(regexp = "\\d{1,8}", message = "El DNI debe contener solo números y máximo 8 dígitos.")
    private String usuario;

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

    @NotBlank(message = "{message.required}")
    private String password;

    @NotNull(message = "{message.required}")
    private List<Integer> idsRoles;
}
