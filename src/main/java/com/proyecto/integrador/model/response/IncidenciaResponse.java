package com.proyecto.integrador.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidenciaResponse {
    private Integer idIncidencia;
    private String codigo;
    private String reportante;
    private String responsable;
    private String categoria;
    private String ubicacion;
    private String severidad;
    private String prioridad;
    private String estado;
    private Integer puntajePrioridad;
    private String titulo;
    private String fechaRegistro;
    private String fechaLimite;
    private String fechaResolucion;
    private String fechaCierre;
}
