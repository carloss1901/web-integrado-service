package com.proyecto.integrador.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenciaHistorialResponse {
    private Integer idHistorial;
    private Integer idIncidencia;
    private Integer idUsuario;
    private String usuario;
    private String rol;
    private String tipoEvento;
    private String valorAnterior;
    private String valorNuevo;
    private String descripcion;
    private String fechaEvento;
}