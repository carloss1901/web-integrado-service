package com.proyecto.integrador.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Generated;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class ErrorGenerico {
    private Integer tipMen;
    private String mensaje;
    private String codigo;
    private List<ErrorCampo> errores = Collections.emptyList();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String logerror;

    public ErrorGenerico(TypeMessage tipMen, String message, HttpStatus status) {
        this.tipMen = tipMen.getValue();
        this.mensaje = message;
        this.codigo = String.valueOf(status.value());
    }

    @Generated
    public Integer getTipMen() {
        return this.tipMen;
    }

    @Generated
    public String getMensaje() {
        return this.mensaje;
    }

    @Generated
    public String getCodigo() {
        return this.codigo;
    }

    @Generated
    public List<ErrorCampo> getErrores() {
        return this.errores;
    }

    @Generated
    public String getLogerror() {
        return this.logerror;
    }
}
