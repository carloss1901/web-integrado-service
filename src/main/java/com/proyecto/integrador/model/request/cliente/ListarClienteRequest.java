package com.proyecto.integrador.model.request.cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListarClienteRequest {
    private String nombre;
    private String codExterno;
    private Short estado;
    private Integer page;
    private Integer size;
}
