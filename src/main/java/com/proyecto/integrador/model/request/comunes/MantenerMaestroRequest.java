package com.proyecto.integrador.model.request.comunes;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MantenerMaestroRequest {
    private Integer idMaestro;
    private String descripcion;
    private String detalle;
    private Integer nivel;
    private BigDecimal puntajeMinimo;
    private BigDecimal puntajeMaximo;
    private Integer idSeveridad;
    private Integer minutosResolucion;
}
