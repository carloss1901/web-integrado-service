package com.proyecto.integrador.model.enums;

public enum TipoMaestro {
    ROLES("roles"),
    CATEGORIAS("categorias"),
    UBICACIONES("ubicaciones"),
    SEVERIDADES("severidades"),
    PRIORIDADES("prioridades"),
    ESTADOS_INCIDENCIA("estados-incidencia"),
    SLA("sla");

    private final String valor;

    TipoMaestro(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipoMaestro fromValor(String valor) {
        for (TipoMaestro tipo : values()) {
            if (tipo.valor.equalsIgnoreCase(valor == null ? "" : valor.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Maestro no soportado");
    }
}