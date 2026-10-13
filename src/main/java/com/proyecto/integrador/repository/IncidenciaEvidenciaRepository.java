package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaEvidenciaEntity;
import com.proyecto.integrador.model.projection.IncidenciaEvidenciaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenciaEvidenciaRepository
        extends JpaRepository<IncidenciaEvidenciaEntity, Integer> {

    @Query(
            value = """
            SELECT
                e.idEvidencia AS idEvidencia,
                e.idIncidencia AS idIncidencia,
                e.idUsuario AS idUsuario,
                CONCAT(CONCAT(u.nombres, ' '), u.apellidos) AS usuario,
                r.nombre AS rol,
                e.nombreArchivo AS nombreArchivo,
                e.rutaArchivo AS rutaArchivo,
                e.descripcion AS descripcion,
                CAST(e.fechaRegistro AS string) AS fechaRegistro
            FROM IncidenciaEvidenciaEntity e
            JOIN UsuarioEntity u
              ON u.idUsuario = e.idUsuario
            LEFT JOIN UsuarioRolEntity ur
              ON ur.idUsuario = u.idUsuario
             AND ur.activo = true
             AND ur.estado = true
            LEFT JOIN RolEntity r
              ON r.idRol = ur.idRol
             AND r.activo = true
            WHERE e.idIncidencia = :idIncidencia
              AND e.activo = true
            ORDER BY e.fechaRegistro
            """,
            countQuery = """
            SELECT COUNT(e)
            FROM IncidenciaEvidenciaEntity e
            WHERE e.idIncidencia = :idIncidencia
              AND e.activo = true
            """
    )
    Page<IncidenciaEvidenciaProjection> listarEvidencias(
            @Param("idIncidencia") Integer idIncidencia,
            Pageable pageable
    );
}