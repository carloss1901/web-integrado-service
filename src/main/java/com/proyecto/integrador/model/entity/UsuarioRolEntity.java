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
@Table(name = "USUARIO_ROL")
public class UsuarioRolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO_ROL")
    private Integer idUsuarioRol;

    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "ID_ROL")
    private Integer idRol;

    @Column(name = "ESTADO")
    private Boolean estado;

    @Column(name = "FEC_REG", insertable = false, updatable = false)
    private LocalDateTime fecReg;

    @Column(name = "FEC_MOD")
    private LocalDateTime fecMod;
}
