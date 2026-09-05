package com.proyecto.integrador.model.projection;

public interface IncidenciaEvidenciaProjection {
    Integer getIdEvidencia();
    Integer getIdIncidencia();
    Integer getIdUsuario();
    String getUsuario();
    String getRol();
    String getNombreArchivo();
    String getRutaArchivo();
    String getDescripcion();
    String getFechaRegistro();
}