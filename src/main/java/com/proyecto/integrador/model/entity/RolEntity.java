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
@Table(name = "ROL")
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Integer idRol;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "FEC_REG", insertable = false, updatable = false)
    private LocalDateTime fecReg;

    @Column(name = "FEC_MOD")
    private LocalDateTime fecMod;
}
