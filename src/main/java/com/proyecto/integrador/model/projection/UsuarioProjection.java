package com.proyecto.integrador.model.projection;

public interface UsuarioProjection {
    Integer getIdUsuario();
    String getUsuario();
    String getNombres();
    String getApePaterno();
    String getApeMaterno();
    String getApellidos();
    String getCorreo();
    Boolean getActivo();
    String getEstadoDsc();
    String getRoles();
    String getFecRegistro();
    String getFecModificacion();
}
