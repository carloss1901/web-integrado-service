package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarEstadoIncidenciaRequest {

    @NotNull(message = "{message.required}")
    private Integer idIncidencia;

    @NotBlank(message = "{message.required}")
    @Size(max = 1000, message = "{message.maxLength}")
    private String observacion;

    private Integer idUsuario;
}
