package com.proyecto.integrador.model.projection;

public interface ClienteProjection {
    Integer getIdCliente();
    String getNombre();
    String getCodExterno();
    String getDireccion();
    String getCorreo();
    Boolean getActivo();
    String getEstadoDsc();
    String getFecCreacion();
    String getFecModificacion();
}
