package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaHistorialEntity;
import com.proyecto.integrador.model.projection.IncidenciaHistorialProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenciaHistorialRepository
        extends JpaRepository<IncidenciaHistorialEntity, Integer> {

    @Query(
            value = """
            SELECT
                h.idHistorial AS idHistorial,
                h.idIncidencia AS idIncidencia,
                h.idUsuario AS idUsuario,
                CONCAT(CONCAT(u.nombres, ' '), u.apellidos) AS usuario,
                r.nombre AS rol,
                h.tipoEvento AS tipoEvento,
                h.valorAnterior AS valorAnterior,
                h.valorNuevo AS valorNuevo,
                h.descripcion AS descripcion,
                CAST(h.fechaEvento AS string) AS fechaEvento
            FROM IncidenciaHistorialEntity h
            JOIN UsuarioEntity u
              ON u.idUsuario = h.idUsuario
            LEFT JOIN UsuarioRolEntity ur
              ON ur.idUsuario = u.idUsuario
             AND ur.activo = true
             AND ur.estado = true
            LEFT JOIN RolEntity r
              ON r.idRol = ur.idRol
             AND r.activo = true
            WHERE h.idIncidencia = :idIncidencia
              AND h.activo = true
            ORDER BY h.fechaEvento
            """,
            countQuery = """
            SELECT COUNT(h)
            FROM IncidenciaHistorialEntity h
            WHERE h.idIncidencia = :idIncidencia
              AND h.activo = true
            """
    )
    Page<IncidenciaHistorialProjection> listarHistorial(
            @Param("idIncidencia") Integer idIncidencia,
            Pageable pageable
    );
}