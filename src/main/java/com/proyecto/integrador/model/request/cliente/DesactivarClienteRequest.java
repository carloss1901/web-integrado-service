package com.proyecto.integrador.model.request.cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesactivarClienteRequest {
    private Integer idCliente;
    private Boolean activar;
}
