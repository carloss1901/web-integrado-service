package com.proyecto.integrador.model.request.incidencia;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarIncidenciaRequest {
    private Integer idReportante;
    private Integer idCategoria;
    private Integer idUbicacion;
    private String titulo;
    private String descripcion;
}
