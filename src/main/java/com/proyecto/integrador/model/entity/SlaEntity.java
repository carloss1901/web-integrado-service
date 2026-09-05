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
@Table(name = "SLA")
public class SlaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SLA")
    private Integer idSla;

    @Column(name = "ID_SEVERIDAD")
    private Integer idSeveridad;

    @Column(name = "MINUTOS_RESOLUCION")
    private Integer minutosResolucion;

    @Column(name = "FEC_REG", insertable = false, updatable = false)
    private LocalDateTime fecReg;

    @Column(name = "FEC_MOD")
    private LocalDateTime fecMod;
}
