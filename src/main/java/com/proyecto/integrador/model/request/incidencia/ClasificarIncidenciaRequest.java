package com.proyecto.integrador.model.request.incidencia;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClasificarIncidenciaRequest {
    private Integer idIncidencia;
    private Integer idUsuario;
    private Integer idSeveridad;
    private Integer impacto;
    private Integer urgencia;
    private Integer reincidencia;
}
