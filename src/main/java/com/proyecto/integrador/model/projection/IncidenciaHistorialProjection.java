package com.proyecto.integrador.model.projection;

public interface IncidenciaHistorialProjection {
    Integer getIdHistorial();
    Integer getIdIncidencia();
    Integer getIdUsuario();
    String getUsuario();
    String getRol();
    String getTipoEvento();
    String getValorAnterior();
    String getValorNuevo();
    String getDescripcion();
    String getFechaEvento();
}