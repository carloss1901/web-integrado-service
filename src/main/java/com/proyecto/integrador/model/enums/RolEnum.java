package com.proyecto.integrador.model.enums;

import java.util.Arrays;

public enum RolEnum {
    REPORTANTE(1, "REPORTANTE", "Reportante"),
    OPERADOR_MESA_CONTROL(2, "OPERADOR_MESA_CONTROL", "Operador de mesa de control"),
    RESPONSABLE_ATENCION(3, "RESPONSABLE_ATENCION", "Responsable de atención"),
    SUPERVISOR(4, "SUPERVISOR", "Supervisor"),
    ADMINISTRADOR(5, "ADMINISTRADOR", "Administrador");

    private final Integer id;
    private final String codigo;
    private final String descripcion;

    RolEnum(Integer id, String codigo, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static RolEnum fromId(Integer id) {
        return Arrays.stream(values())
                .filter(rol -> rol.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    public static RolEnum fromCodigo(String codigo) {
        return Arrays.stream(values())
                .filter(rol -> rol.codigo.equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }
}
