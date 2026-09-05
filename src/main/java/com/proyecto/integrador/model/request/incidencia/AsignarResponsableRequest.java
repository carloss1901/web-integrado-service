package com.proyecto.integrador.model.request.incidencia;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignarResponsableRequest {
    private Integer idIncidencia;
    private Integer idUsuario;
    private Integer idResponsable;
}
