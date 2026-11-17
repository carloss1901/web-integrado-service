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
@Table(name = "INCIDENCIA_REVISION_SUPERVISOR")
public class IncidenciaRevisionSupervisorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REVISION")
    private Integer idRevision;
    @Column(name = "ID_INCIDENCIA")
    private Integer idIncidencia;
    @Column(name = "ID_SUPERVISOR")
    private Integer idSupervisor;
    @Column(name = "APROBADA")
    private Boolean aprobada;
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "FECHA_REVISION")
    private LocalDateTime fechaRevision;
    @Column(name = "ACTIVO")
    private Boolean activo;
}
