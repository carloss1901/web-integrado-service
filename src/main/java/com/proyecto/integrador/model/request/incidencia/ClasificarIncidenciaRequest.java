package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class ClasificarIncidenciaRequest {
    @NotNull(message = "{message.required}")
    private Integer idIncidencia;

    @Schema(hidden = true)
    private Integer idUsuario;

    @NotNull(message = "{message.required}")
    private Integer idSeveridad;

    @NotNull(message = "{message.required}")
    @Min(value = 1, message = "{message.minOne}")
    @Max(value = 4, message = "{message.maxFour}")
    private Integer impacto;

    @NotNull(message = "{message.required}")
    @Min(value = 1, message = "{message.minOne}")
    @Max(value = 4, message = "{message.maxFour}")
    private Integer urgencia;

}
