package com.proyecto.integrador.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenciaEvidenciaResponse {
    private Integer idEvidencia;
    private Integer idIncidencia;
    private Integer idUsuario;
    private String usuario;
    private String rol;
    private String nombreArchivo;
    private String rutaArchivo;
    private String descripcion;
    private String fechaRegistro;
}