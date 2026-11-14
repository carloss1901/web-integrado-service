package com.proyecto.integrador.model.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeleccionarRolRequest {

    @NotNull(message = "{message.required}")
    private Integer idRol;
}
