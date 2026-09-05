package com.proyecto.integrador.model.request.incidencia;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoIncidenciaRequest {
    private Integer idIncidencia;
    private Integer idUsuario;
}
