package com.proyecto.integrador.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenciaComentarioResponse {
    private Integer idComentario;
    private Integer idIncidencia;
    private Integer idUsuario;
    private String usuario;
    private String rol;
    private String comentario;
    private String fechaRegistro;
}