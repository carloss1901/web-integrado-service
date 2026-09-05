package com.proyecto.integrador.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "INCIDENCIA_HISTORIAL")
public class IncidenciaHistorialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HISTORIAL")
    private Integer idHistorial;

    @Column(name = "ID_INCIDENCIA")
    private Integer idIncidencia;

    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "TIPO_EVENTO")
    private String tipoEvento;

    @Column(name = "VALOR_ANTERIOR")
    private String valorAnterior;

    @Column(name = "VALOR_NUEVO")
    private String valorNuevo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "FECHA_EVENTO")
    private LocalDateTime fechaEvento;

    @Column(name = "ACTIVO")
    private Boolean activo;

    @Column(name = "FEC_REG", insertable = false, updatable = false)
    private LocalDateTime fecReg;

    @Column(name = "FEC_MOD")
    private LocalDateTime fecMod;
}
