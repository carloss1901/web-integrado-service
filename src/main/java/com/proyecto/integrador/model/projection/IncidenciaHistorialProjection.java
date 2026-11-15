package com.proyecto.integrador.model.projection;

import java.math.BigDecimal;

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
    Integer getImpacto();
    Integer getUrgencia();
    Integer getReincidencia();
    BigDecimal getPuntajePrioridad();
    String getFechaEvento();
}
