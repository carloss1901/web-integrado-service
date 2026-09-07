package com.proyecto.integrador.model.projection;

public interface IncidenciaAccionCorrectivaProjection {
    Integer getIdAccionCorrectiva();
    Integer getIdIncidencia();
    Integer getIdUsuario();
    String getUsuario();
    String getRol();
    String getDescripcion();
    String getFechaRegistro();
}