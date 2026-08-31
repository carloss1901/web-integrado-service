package com.proyecto.integrador.model.request.cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarClienteRequest {
    private Integer idCliente;
    private String nombre;
    private String codExterno;
    private String direccion;
    private String correo;
}
