package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class AsignarResponsableRequest {
    @NotNull(message = "{message.required}")
    private Integer idIncidencia;

    @Schema(hidden = true)
    private Integer idUsuario;

    @NotNull(message = "{message.required}")
    private Integer idResponsable;
}
