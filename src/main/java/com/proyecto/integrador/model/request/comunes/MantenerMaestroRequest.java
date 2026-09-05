package com.proyecto.integrador.model.request.comunes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MantenerMaestroRequest {
    private Integer idMaestro;
    private String descripcion;
    private String detalle;
    private Integer nivel;
    private Integer puntajeMinimo;
    private Integer puntajeMaximo;
    private Integer idSeveridad;
    private Integer minutosResolucion;
}
