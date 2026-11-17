package com.proyecto.integrador.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CargaResponsableResponse {
    private String responsable;
    private Long incidenciasActivas;
}
