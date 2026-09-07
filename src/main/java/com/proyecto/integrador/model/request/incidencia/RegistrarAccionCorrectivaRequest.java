package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarAccionCorrectivaRequest {
    @NotNull(message = "{message.required}")
    private Integer idIncidencia;

    @NotNull(message = "{message.required}")
    private Integer idUsuario;

    @NotBlank(message = "{message.required}")
    private String descripcion;
}