package com.proyecto.integrador.model.projection;

public interface IncidenciaComentarioProjection {
    Integer getIdComentario();
    Integer getIdIncidencia();
    Integer getIdUsuario();
    String getUsuario();
    String getRol();
    String getComentario();
    String getFechaRegistro();
}