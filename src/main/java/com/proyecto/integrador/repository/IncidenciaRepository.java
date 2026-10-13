package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaEntity;
import com.proyecto.integrador.model.projection.IncidenciaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.proyecto.integrador.model.response.IncidenciaBusquedaResponse;
import com.proyecto.integrador.model.response.IndicadoresIncidenciaResponse;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Repository
public interface IncidenciaRepository extends JpaRepository<IncidenciaEntity, Integer> {

    @Query(
            value = """
        SELECT
            i.idIncidencia AS idIncidencia,
            i.codigo AS codigo,
            CONCAT(CONCAT(ur.nombres, ' '), ur.apellidos) AS reportante,
            CONCAT(CONCAT(ua.nombres, ' '), ua.apellidos) AS responsable,
            c.nombre AS categoria,
            u.nombre AS ubicacion,
            s.nombre AS severidad,
            p.nombre AS prioridad,
            e.nombre AS estado,
            i.puntajePrioridad AS puntajePrioridad,
            i.titulo AS titulo,
            CAST(i.fechaRegistro AS string) AS fechaRegistro,
            CAST(i.fechaLimite AS string) AS fechaLimite,
            CAST(i.fechaResolucion AS string) AS fechaResolucion,
            CAST(i.fechaCierre AS string) AS fechaCierre
        FROM IncidenciaEntity i
        JOIN UsuarioEntity ur
          ON ur.idUsuario = i.idReportante
        LEFT JOIN UsuarioEntity ua
          ON ua.idUsuario = i.idResponsable
        JOIN CategoriaEntity c
          ON c.idCategoria = i.idCategoria
        JOIN UbicacionEntity u
          ON u.idUbicacion = i.idUbicacion
        LEFT JOIN SeveridadEntity s
          ON s.idSeveridad = i.idSeveridad
        LEFT JOIN PrioridadEntity p
          ON p.idPrioridad = i.idPrioridad
        JOIN EstadoIncidenciaEntity e
          ON e.idEstado = i.idEstado
        WHERE i.activo = true
          AND i.fechaLimite IS NOT NULL
          AND i.fechaLimite < CURRENT_TIMESTAMP
          AND i.idEstado NOT IN (6, 7, 8)
        ORDER BY i.fechaLimite
        """,
            countQuery = """
        SELECT COUNT(i)
        FROM IncidenciaEntity i
        WHERE i.activo = true
          AND i.fechaLimite IS NOT NULL
          AND i.fechaLimite < CURRENT_TIMESTAMP
          AND i.idEstado NOT IN (6, 7, 8)
        """
    )
    Page<IncidenciaProjection> listarIncidenciasVencidas(Pageable pageable);

    @Query("""
    SELECT
        i.idIncidencia AS idIncidencia,
        i.codigo AS codigo,
        CONCAT(CONCAT(ur.nombres, ' '), ur.apellidos) AS reportante,
        CONCAT(CONCAT(ua.nombres, ' '), ua.apellidos) AS responsable,
        c.nombre AS categoria,
        u.nombre AS ubicacion,
        s.nombre AS severidad,
        p.nombre AS prioridad,
        e.nombre AS estado,
        i.puntajePrioridad AS puntajePrioridad,
        i.titulo AS titulo,
        CAST(i.fechaRegistro AS string) AS fechaRegistro,
        CAST(i.fechaLimite AS string) AS fechaLimite,
        CAST(i.fechaResolucion AS string) AS fechaResolucion,
        CAST(i.fechaCierre AS string) AS fechaCierre
    FROM IncidenciaEntity i
    JOIN UsuarioEntity ur
      ON ur.idUsuario = i.idReportante
    LEFT JOIN UsuarioEntity ua
      ON ua.idUsuario = i.idResponsable
    JOIN CategoriaEntity c
      ON c.idCategoria = i.idCategoria
    JOIN UbicacionEntity u
      ON u.idUbicacion = i.idUbicacion
    LEFT JOIN SeveridadEntity s
      ON s.idSeveridad = i.idSeveridad
    LEFT JOIN PrioridadEntity p
      ON p.idPrioridad = i.idPrioridad
    JOIN EstadoIncidenciaEntity e
      ON e.idEstado = i.idEstado
    WHERE i.activo = true
    ORDER BY i.fechaRegistro DESC
    """)
    List<IncidenciaProjection> listarIncidencias();

    @Query("""
    SELECT
        i.idIncidencia AS idIncidencia,
        i.codigo AS codigo,
        CONCAT(CONCAT(ur.nombres, ' '), ur.apellidos) AS reportante,
        CONCAT(CONCAT(ua.nombres, ' '), ua.apellidos) AS responsable,
        c.nombre AS categoria,
        u.nombre AS ubicacion,
        s.nombre AS severidad,
        p.nombre AS prioridad,
        e.nombre AS estado,
        i.puntajePrioridad AS puntajePrioridad,
        i.titulo AS titulo,
        CAST(i.fechaRegistro AS string) AS fechaRegistro,
        CAST(i.fechaLimite AS string) AS fechaLimite,
        CAST(i.fechaResolucion AS string) AS fechaResolucion,
        CAST(i.fechaCierre AS string) AS fechaCierre
    FROM IncidenciaEntity i
    JOIN UsuarioEntity ur
      ON ur.idUsuario = i.idReportante
    LEFT JOIN UsuarioEntity ua
      ON ua.idUsuario = i.idResponsable
    JOIN CategoriaEntity c
      ON c.idCategoria = i.idCategoria
    JOIN UbicacionEntity u
      ON u.idUbicacion = i.idUbicacion
    LEFT JOIN SeveridadEntity s
      ON s.idSeveridad = i.idSeveridad
    LEFT JOIN PrioridadEntity p
      ON p.idPrioridad = i.idPrioridad
    JOIN EstadoIncidenciaEntity e
      ON e.idEstado = i.idEstado
    WHERE i.activo = true
      AND i.idIncidencia = :idIncidencia
    """)
    Optional<IncidenciaProjection> obtenerIncidencia(
            @Param("idIncidencia") Integer idIncidencia
    );

    @Query("""
        SELECT new com.proyecto.integrador.model.response.IncidenciaBusquedaResponse(
            i.idIncidencia,
            i.codigo,
            i.titulo,
            i.fechaRegistro
        )
        FROM IncidenciaEntity i
        WHERE i.activo = true
          AND LOWER(i.titulo)
              LIKE LOWER(CONCAT('%', :texto, '%'))
        ORDER BY i.fechaRegistro DESC
        """)
    List<IncidenciaBusquedaResponse> buscarResumenPorTitulo(
            @Param("texto") String texto
    );

    @Query("""
        SELECT new com.proyecto.integrador.model.response.IncidenciaBusquedaResponse(
            i.idIncidencia,
            i.codigo,
            i.titulo,
            i.fechaRegistro
        )
        FROM IncidenciaEntity i
        WHERE i.activo = true
          AND i.fechaRegistro BETWEEN :inicio AND :fin
        ORDER BY i.fechaRegistro DESC
        """)
    List<IncidenciaBusquedaResponse> buscarPorRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("""
    SELECT new com.proyecto.integrador.model.response.IndicadoresIncidenciaResponse(
        COUNT(i),
        COALESCE(
            SUM(
                CASE
                    WHEN i.idEstado NOT IN (6, 7, 8) THEN 1
                    ELSE 0
                END
            ),
            0
        ),
        COALESCE(
            SUM(
                CASE
                    WHEN i.fechaLimite IS NOT NULL
                     AND i.fechaLimite < CURRENT_TIMESTAMP
                     AND i.idEstado NOT IN (6, 7, 8)
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ),
        COALESCE(
            SUM(
                CASE
                    WHEN i.idEstado = 6 THEN 1
                    ELSE 0
                END
            ),
            0
        ),
        COALESCE(
            SUM(
                CASE
                    WHEN i.idEstado = 7 THEN 1
                    ELSE 0
                END
            ),
            0
        )
    )
    FROM IncidenciaEntity i
    WHERE i.activo = true
    """)
    IndicadoresIncidenciaResponse obtenerIndicadores();
}
