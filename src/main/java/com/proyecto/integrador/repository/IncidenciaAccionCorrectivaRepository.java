package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaAccionCorrectivaEntity;
import com.proyecto.integrador.model.projection.IncidenciaAccionCorrectivaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenciaAccionCorrectivaRepository
        extends JpaRepository<IncidenciaAccionCorrectivaEntity, Integer> {

    @Query(
            value = """
            SELECT
                a.idAccionCorrectiva AS idAccionCorrectiva,
                a.idIncidencia AS idIncidencia,
                a.idUsuario AS idUsuario,
                CONCAT(CONCAT(u.nombres, ' '), u.apellidos) AS usuario,
                r.nombre AS rol,
                a.descripcion AS descripcion,
                CAST(a.fechaRegistro AS string) AS fechaRegistro
            FROM IncidenciaAccionCorrectivaEntity a
            JOIN UsuarioEntity u
              ON u.idUsuario = a.idUsuario
            LEFT JOIN UsuarioRolEntity ur
              ON ur.idUsuario = u.idUsuario
             AND ur.activo = true
             AND ur.estado = true
            LEFT JOIN RolEntity r
              ON r.idRol = ur.idRol
             AND r.activo = true
            WHERE a.idIncidencia = :idIncidencia
              AND a.activo = true
            ORDER BY a.fechaRegistro
            """,
            countQuery = """
            SELECT COUNT(a)
            FROM IncidenciaAccionCorrectivaEntity a
            WHERE a.idIncidencia = :idIncidencia
              AND a.activo = true
            """
    )
    Page<IncidenciaAccionCorrectivaProjection> listarAccionesCorrectivas(
            @Param("idIncidencia") Integer idIncidencia,
            Pageable pageable
    );
}