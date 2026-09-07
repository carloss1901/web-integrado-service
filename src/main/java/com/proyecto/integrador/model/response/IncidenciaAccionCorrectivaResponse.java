package com.proyecto.integrador.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenciaAccionCorrectivaResponse {
    private Integer idAccionCorrectiva;
    private Integer idIncidencia;
    private Integer idUsuario;
    private String usuario;
    private String rol;
    private String descripcion;
    private String fechaRegistro;
}