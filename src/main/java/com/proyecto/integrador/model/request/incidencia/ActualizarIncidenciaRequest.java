package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class ActualizarIncidenciaRequest {
    @Schema(hidden = true)
    private Integer idUsuario;

    @NotNull(message = "{message.required}")
    private Integer idIncidencia;

    @NotNull(message = "{message.required}")
    private Integer idCategoria;

    @NotNull(message = "{message.required}")
    private Integer idUbicacion;

    @NotBlank(message = "{message.required}")
    private String titulo;

    @NotBlank(message = "{message.required}")
    private String descripcion;
}
