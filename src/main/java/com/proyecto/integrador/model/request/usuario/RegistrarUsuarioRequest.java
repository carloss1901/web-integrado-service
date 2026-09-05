package com.proyecto.integrador.model.request.usuario;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistrarUsuarioRequest {
    private String usuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String password;
    private List<Integer> idsRoles;
}
