package com.proyecto.integrador.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IndicadoresIncidenciaResponse {

    private Long totalIncidencias;
    private Long incidenciasPendientes;
    private Long incidenciasVencidas;
    private Long incidenciasResueltas;
    private Long incidenciasCerradas;
}