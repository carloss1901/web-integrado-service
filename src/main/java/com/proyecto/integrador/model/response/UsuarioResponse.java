package com.proyecto.integrador.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsuarioResponse {
    private Integer idUsuario;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private Boolean estado;
    private Boolean activo;
    private List<Integer> idsRoles;
}
