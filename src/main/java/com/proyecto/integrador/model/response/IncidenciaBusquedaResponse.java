package com.proyecto.integrador.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class IncidenciaBusquedaResponse {

    private Integer idIncidencia;
    private String codigo;
    private String titulo;
    private String fechaRegistro;

    public IncidenciaBusquedaResponse() {
    }

    public IncidenciaBusquedaResponse(
            Integer idIncidencia,
            String codigo,
            String titulo,
            LocalDateTime fechaRegistro
    ) {
        this.idIncidencia = idIncidencia;
        this.codigo = codigo;
        this.titulo = titulo;

        if (fechaRegistro != null) {
            this.fechaRegistro = fechaRegistro.format(
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy HH:mm"
                    )
            );
        }
    }
}