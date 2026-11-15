package com.proyecto.integrador.model.projection;

import java.math.BigDecimal;

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
    Integer getImpacto();
    Integer getUrgencia();
    Integer getReincidencia();
    BigDecimal getPuntuacionPrioridad();
    String getExplicacionPrioridad();
    String getTitulo();
    String getFechaRegistro();
    String getFechaLimite();
    String getFechaResolucion();
    String getFechaCierre();
}
