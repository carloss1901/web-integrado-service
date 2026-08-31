package com.proyecto.integrador.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "CLIENTE")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    private Integer idCliente;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "COD_EXTERNO")
    private String codExterno;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "CORREO_ELECTRONICO")
    private String correoElectronico;

    @Column(name = "ACTIVO")
    private Boolean activo;

    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;

    @Column(name = "FECHA_MODIFICACION")
    private Date fechaModificacion;
}
