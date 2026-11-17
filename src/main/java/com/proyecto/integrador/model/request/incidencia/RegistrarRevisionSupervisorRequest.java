package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarRevisionSupervisorRequest {
    @NotNull(message = "{message.required}")
    private Integer idIncidencia;
    @NotNull(message = "{message.required}")
    private Boolean aprobada;
    @NotBlank(message = "{message.required}")
    private String observacion;
    private Integer idUsuario;
}
