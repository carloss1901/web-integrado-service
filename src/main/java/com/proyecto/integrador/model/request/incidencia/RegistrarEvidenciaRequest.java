package com.proyecto.integrador.model.request.incidencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class RegistrarEvidenciaRequest {
    @NotNull(message = "{message.required}")
    private Integer idIncidencia;

    @Schema(hidden = true)
    private Integer idUsuario;

    @NotBlank(message = "{message.required}")
    private String nombreArchivo;

    @NotBlank(message = "{message.required}")
    private String rutaArchivo;

    private String descripcion;
}
