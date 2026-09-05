package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignarResponsableRequest {
    @NotNull(message = "{message.required}")
    private Integer idIncidencia;

    @NotNull(message = "{message.required}")
    private Integer idUsuario;

    @NotNull(message = "{message.required}")
    private Integer idResponsable;
}
