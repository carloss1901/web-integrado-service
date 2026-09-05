package com.proyecto.integrador.model.projection;

public interface IncidenciaProjection {
    Integer getIdIncidencia();
    String getCodigo();
    String getReportante();
    String getResponsable();
    String getCategoria();
    String getUbicacion();
    String getSeveridad();
    String getPrioridad();
    String getEstado();
    Integer getPuntajePrioridad();
    String getTitulo();
    String getFechaRegistro();
    String getFechaLimite();
    String getFechaResolucion();
    String getFechaCierre();
}
