package com.proyecto.integrador.util;

import lombok.Generated;

import java.io.Serializable;

public class ErrorCampo implements Serializable {
    private String campo;
    private String mensaje;

    @Generated
    public ErrorCampo() {
    }

    @Generated
    public ErrorCampo(final String campo, final String mensaje) {
        this.campo = campo;
        this.mensaje = mensaje;
    }

    @Generated
    public String getCampo() {
        return this.campo;
    }

    @Generated
    public String getMensaje() {
        return this.mensaje;
    }
}
