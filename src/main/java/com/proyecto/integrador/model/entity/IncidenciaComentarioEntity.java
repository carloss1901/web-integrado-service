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
@Table(name = "INCIDENCIA_COMENTARIO")
public class IncidenciaComentarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COMENTARIO")
    private Integer idComentario;

    @Column(name = "ID_INCIDENCIA")
    private Integer idIncidencia;

    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "COMENTARIO")
    private String comentario;

    @Column(name = "FECHA_REGISTRO")
    private LocalDateTime fechaRegistro;

    @Column(name = "ACTIVO")
    private Boolean activo;

    @Column(name = "FEC_REG", insertable = false, updatable = false)
    private LocalDateTime fecReg;

    @Column(name = "FEC_MOD")
    private LocalDateTime fecMod;
}