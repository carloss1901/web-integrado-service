package com.proyecto.integrador.model.request.incidencia;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarEvidenciaRequest {
    private Integer idIncidencia;
    private Integer idUsuario;
    private String nombreArchivo;
    private String rutaArchivo;
    private String descripcion;
}
