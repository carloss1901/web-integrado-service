package com.proyecto.integrador.model.request.usuario;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListarUsuarioRequest {
    private String usuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private Short estado;

    @NotNull(message = "{message.required}")
    @Min(value = 1, message = "{message.minOne}")
    private Integer page;

    @NotNull(message = "{message.required}")
    @Min(value = 1, message = "{message.minOne}")
    private Integer size;
}
