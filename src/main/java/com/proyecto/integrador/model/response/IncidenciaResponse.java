package com.proyecto.integrador.model.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

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
    private Integer impacto;
    private Integer urgencia;
    private Integer reincidencia;
    private BigDecimal puntuacionPrioridad;
    private String explicacionPrioridad;
    private String titulo;
    private String descripcion;
    private String fechaRegistro;
    private String fechaLimite;
    private String fechaResolucion;
    private String fechaCierre;
}
