package com.proyecto.integrador.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "INCIDENCIA")
public class IncidenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INCIDENCIA")
    private Integer idIncidencia;

    @Column(name = "CODIGO")
    private String codigo;

    @Column(name = "ID_REPORTANTE")
    private Integer idReportante;

    @Column(name = "ID_RESPONSABLE")
    private Integer idResponsable;

    @Column(name = "ID_CATEGORIA")
    private Integer idCategoria;

    @Column(name = "ID_UBICACION")
    private Integer idUbicacion;

    @Column(name = "ID_SEVERIDAD")
    private Integer idSeveridad;

    @Column(name = "ID_PRIORIDAD")
    private Integer idPrioridad;

    @Column(name = "ID_ESTADO")
    private Integer idEstado;

    @Column(name = "ID_SLA")
    private Integer idSla;

    @Column(name = "IMPACTO")
    private Integer impacto;

    @Column(name = "URGENCIA")
    private Integer urgencia;

    @Column(name = "REINCIDENCIA")
    private Integer reincidencia;

    @Column(name = "PUNTAJE_PRIORIDAD")
    private Integer puntajePrioridad;

    @Column(name = "TITULO")
    private String titulo;

    @Lob
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "FECHA_REGISTRO")
    private LocalDateTime fechaRegistro;

    @Column(name = "FECHA_LIMITE")
    private LocalDateTime fechaLimite;

    @Column(name = "FECHA_RESOLUCION")
    private LocalDateTime fechaResolucion;

    @Column(name = "FECHA_CIERRE")
    private LocalDateTime fechaCierre;

    @Column(name = "ACTIVO")
    private Boolean activo;

    @Column(name = "FEC_REG", insertable = false, updatable = false)
    private LocalDateTime fecReg;

    @Column(name = "FEC_MOD")
    private LocalDateTime fecMod;
}
