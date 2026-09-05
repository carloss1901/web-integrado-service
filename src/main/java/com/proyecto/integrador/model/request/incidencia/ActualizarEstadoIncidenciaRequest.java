package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoIncidenciaRequest {
    @NotNull(message = "{message.required}")
    private Integer idIncidencia;

    @NotNull(message = "{message.required}")
    private Integer idUsuario;
}
