package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClasificarIncidenciaRequest {
    @NotNull(message = "{message.required}")
    private Integer idIncidencia;

    @NotNull(message = "{message.required}")
    private Integer idUsuario;

    @NotNull(message = "{message.required}")
    private Integer idSeveridad;

    @NotNull(message = "{message.required}")
    @Min(value = 1, message = "{message.minOne}")
    @Max(value = 3, message = "{message.maxThree}")
    private Integer impacto;

    @NotNull(message = "{message.required}")
    @Min(value = 1, message = "{message.minOne}")
    @Max(value = 3, message = "{message.maxThree}")
    private Integer urgencia;

    @NotNull(message = "{message.required}")
    @Min(value = 0, message = "{message.minZero}")
    @Max(value = 1, message = "{message.maxOne}")
    private Integer reincidencia;
}
