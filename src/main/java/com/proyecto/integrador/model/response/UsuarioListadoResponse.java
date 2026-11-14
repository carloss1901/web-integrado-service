package com.proyecto.integrador.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsuarioListadoResponse {
    private Integer idUsuario;
    private String usuario;
    private String nombres;
    private String apePaterno;
    private String apeMaterno;
    private String correo;
    private Boolean activo;
    private String estadoDsc;
    private List<MaestroResponse> roles;
    private String fecRegistro;
    private String fecModificacion;
}
