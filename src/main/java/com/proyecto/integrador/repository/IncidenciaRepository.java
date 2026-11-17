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
import com.proyecto.integrador.model.response.IndicadorAgrupadoResponse;
import com.proyecto.integrador.model.response.CargaResponsableResponse;

@Repository
public interface IncidenciaRepository extends JpaRepository<IncidenciaEntity, Integer> {

    @Query(
            value = """
        SELECT
            i.idIncidencia AS idIncidencia,
            i.codigo AS codigo,
            CONCAT(CONCAT(ur.nombres, ' '), CONCAT(ur.apePaterno, CONCAT(' ', COALESCE(ur.apeMaterno, '')))) AS reportante,
            CONCAT(CONCAT(ua.nombres, ' '), CONCAT(ua.apePaterno, CONCAT(' ', COALESCE(ua.apeMaterno, '')))) AS responsable,
            c.nombre AS categoria,
            u.nombre AS ubicacion,
            s.nombre AS severidad,
            p.nombre AS prioridad,
            e.nombre AS estado,
            i.impacto AS impacto,
            i.urgencia AS urgencia,
            i.reincidencia AS reincidencia,
            i.puntajePrioridad AS puntuacionPrioridad,
            'Impacto 40%, urgencia 40% y reincidencia 20%' AS explicacionPrioridad,
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

    @Query(value = """
        SELECT i.idIncidencia AS idIncidencia, i.codigo AS codigo,
            CONCAT(CONCAT(ur.nombres, ' '), CONCAT(ur.apePaterno, CONCAT(' ', COALESCE(ur.apeMaterno, '')))) AS reportante,
            CONCAT(CONCAT(ua.nombres, ' '), CONCAT(ua.apePaterno, CONCAT(' ', COALESCE(ua.apeMaterno, '')))) AS responsable,
            c.nombre AS categoria, u.nombre AS ubicacion, s.nombre AS severidad, p.nombre AS prioridad,
            e.nombre AS estado, i.impacto AS impacto, i.urgencia AS urgencia, i.reincidencia AS reincidencia,
            i.puntajePrioridad AS puntuacionPrioridad, 'Impacto 40%, urgencia 40% y reincidencia 20%' AS explicacionPrioridad,
            i.titulo AS titulo, CAST(i.fechaRegistro AS string) AS fechaRegistro, CAST(i.fechaLimite AS string) AS fechaLimite,
            CAST(i.fechaResolucion AS string) AS fechaResolucion, CAST(i.fechaCierre AS string) AS fechaCierre
        FROM IncidenciaEntity i JOIN UsuarioEntity ur ON ur.idUsuario = i.idReportante
        LEFT JOIN UsuarioEntity ua ON ua.idUsuario = i.idResponsable
        JOIN CategoriaEntity c ON c.idCategoria = i.idCategoria JOIN UbicacionEntity u ON u.idUbicacion = i.idUbicacion
        LEFT JOIN SeveridadEntity s ON s.idSeveridad = i.idSeveridad LEFT JOIN PrioridadEntity p ON p.idPrioridad = i.idPrioridad
        JOIN EstadoIncidenciaEntity e ON e.idEstado = i.idEstado
        WHERE i.activo = true AND i.fechaLimite IS NOT NULL AND i.fechaLimite >= :ahora AND i.fechaLimite <= :limite
          AND i.idEstado NOT IN (6, 7, 8) ORDER BY i.fechaLimite
        """, countQuery = """
        SELECT COUNT(i) FROM IncidenciaEntity i WHERE i.activo = true AND i.fechaLimite IS NOT NULL
          AND i.fechaLimite >= :ahora AND i.fechaLimite <= :limite AND i.idEstado NOT IN (6, 7, 8)
        """)
    Page<IncidenciaProjection> listarIncidenciasProximasAVencer(
        @Param("ahora") LocalDateTime ahora, @Param("limite") LocalDateTime limite, Pageable pageable);

    @Query("""
    SELECT
        i.idIncidencia AS idIncidencia,
        i.codigo AS codigo,
        CONCAT(CONCAT(ur.nombres, ' '), CONCAT(ur.apePaterno, CONCAT(' ', COALESCE(ur.apeMaterno, '')))) AS reportante,
        CONCAT(CONCAT(ua.nombres, ' '), CONCAT(ua.apePaterno, CONCAT(' ', COALESCE(ua.apeMaterno, '')))) AS responsable,
        c.nombre AS categoria,
        u.nombre AS ubicacion,
        s.nombre AS severidad,
        p.nombre AS prioridad,
        e.nombre AS estado,
        i.impacto AS impacto,
        i.urgencia AS urgencia,
        i.reincidencia AS reincidencia,
        i.puntajePrioridad AS puntuacionPrioridad,
        'Impacto 40%, urgencia 40% y reincidencia 20%' AS explicacionPrioridad,
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
        SELECT i FROM IncidenciaEntity i WHERE i.activo = true
        AND (:idReportante IS NULL OR i.idReportante = :idReportante)
        AND (:idResponsable IS NULL OR i.idResponsable = :idResponsable)
        AND (:idSeveridad IS NULL OR i.idSeveridad = :idSeveridad)
        AND (:idEstado IS NULL OR i.idEstado = :idEstado)
        AND (:inicio IS NULL OR i.fechaRegistro >= :inicio)
        AND (:fin IS NULL OR i.fechaRegistro <= :fin)
        ORDER BY i.fechaRegistro DESC
        """)
    Page<IncidenciaEntity> listarPaginado(
        @Param("idReportante") Integer idReportante, @Param("idResponsable") Integer idResponsable,
        @Param("idSeveridad") Integer idSeveridad, @Param("idEstado") Integer idEstado,
        @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin, Pageable pageable);

    @Query("""
    SELECT
        i.idIncidencia AS idIncidencia,
        i.codigo AS codigo,
        CONCAT(CONCAT(ur.nombres, ' '), CONCAT(ur.apePaterno, CONCAT(' ', COALESCE(ur.apeMaterno, '')))) AS reportante,
        CONCAT(CONCAT(ua.nombres, ' '), CONCAT(ua.apePaterno, CONCAT(' ', COALESCE(ua.apeMaterno, '')))) AS responsable,
        c.nombre AS categoria,
        u.nombre AS ubicacion,
        s.nombre AS severidad,
        p.nombre AS prioridad,
        e.nombre AS estado,
        i.impacto AS impacto,
        i.urgencia AS urgencia,
        i.reincidencia AS reincidencia,
        i.puntajePrioridad AS puntuacionPrioridad,
        'Impacto 40%, urgencia 40% y reincidencia 20%' AS explicacionPrioridad,
        i.titulo AS titulo,
        i.descripcion AS descripcion,
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
      AND i.idResponsable = :idResponsable
    ORDER BY i.fechaRegistro DESC
    """)
    List<IncidenciaProjection> listarIncidenciasPorResponsable(
        @Param("idResponsable") Integer idResponsable
    );

    @Query("""
    SELECT
        i.idIncidencia AS idIncidencia,
        i.codigo AS codigo,
        CONCAT(CONCAT(ur.nombres, ' '), CONCAT(ur.apePaterno, CONCAT(' ', COALESCE(ur.apeMaterno, '')))) AS reportante,
        CONCAT(CONCAT(ua.nombres, ' '), CONCAT(ua.apePaterno, CONCAT(' ', COALESCE(ua.apeMaterno, '')))) AS responsable,
        c.nombre AS categoria,
        u.nombre AS ubicacion,
        s.nombre AS severidad,
        p.nombre AS prioridad,
        e.nombre AS estado,
        i.impacto AS impacto,
        i.urgencia AS urgencia,
        i.reincidencia AS reincidencia,
        i.puntajePrioridad AS puntuacionPrioridad,
        'Impacto 40%, urgencia 40% y reincidencia 20%' AS explicacionPrioridad,
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
      AND i.idReportante = :idReportante
    ORDER BY i.fechaRegistro DESC
    """)
    List<IncidenciaProjection> listarIncidenciasPorReportante(
        @Param("idReportante") Integer idReportante
    );

    @Query("""
    SELECT
        i.idIncidencia AS idIncidencia,
        i.codigo AS codigo,
        CONCAT(CONCAT(ur.nombres, ' '), CONCAT(ur.apePaterno, CONCAT(' ', COALESCE(ur.apeMaterno, '')))) AS reportante,
        CONCAT(CONCAT(ua.nombres, ' '), CONCAT(ua.apePaterno, CONCAT(' ', COALESCE(ua.apeMaterno, '')))) AS responsable,
        c.nombre AS categoria,
        u.nombre AS ubicacion,
        s.nombre AS severidad,
        p.nombre AS prioridad,
        e.nombre AS estado,
        i.impacto AS impacto,
        i.urgencia AS urgencia,
        i.reincidencia AS reincidencia,
        i.puntajePrioridad AS puntuacionPrioridad,
        'Impacto 40%, urgencia 40% y reincidencia 20%' AS explicacionPrioridad,
        i.titulo AS titulo,
        i.descripcion AS descripcion,
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
        SELECT COUNT(i)
        FROM IncidenciaEntity i
        WHERE i.activo = true
          AND i.idCategoria = :idCategoria
          AND i.idUbicacion = :idUbicacion
          AND i.fechaRegistro BETWEEN :inicio AND :fin
        """)
    long contarIncidenciasRelacionadas(
            @Param("idCategoria") Integer idCategoria,
            @Param("idUbicacion") Integer idUbicacion,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
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
          AND (:idReportante IS NULL OR i.idReportante = :idReportante)
        ORDER BY i.fechaRegistro DESC
        """)
    List<IncidenciaBusquedaResponse> buscarResumenPorTitulo(
            @Param("texto") String texto,
            @Param("idReportante") Integer idReportante
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
          AND (:idReportante IS NULL OR i.idReportante = :idReportante)
        ORDER BY i.fechaRegistro DESC
        """)
    List<IncidenciaBusquedaResponse> buscarPorRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("idReportante") Integer idReportante
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

    @Query("""
        SELECT new com.proyecto.integrador.model.response.IndicadorAgrupadoResponse(u.nombre, COUNT(i))
        FROM IncidenciaEntity i JOIN UbicacionEntity u ON u.idUbicacion = i.idUbicacion
        WHERE i.activo = true AND i.fechaRegistro BETWEEN :inicio AND :fin GROUP BY u.nombre ORDER BY COUNT(i) DESC""")
    List<IndicadorAgrupadoResponse> contarPorZona(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("""
        SELECT new com.proyecto.integrador.model.response.IndicadorAgrupadoResponse(c.nombre, COUNT(i))
        FROM IncidenciaEntity i JOIN CategoriaEntity c ON c.idCategoria = i.idCategoria
        WHERE i.activo = true AND i.fechaRegistro BETWEEN :inicio AND :fin GROUP BY c.nombre ORDER BY COUNT(i) DESC""")
    List<IndicadorAgrupadoResponse> contarPorCategoria(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("""
        SELECT new com.proyecto.integrador.model.response.IndicadorAgrupadoResponse(s.nombre, COUNT(i))
        FROM IncidenciaEntity i LEFT JOIN SeveridadEntity s ON s.idSeveridad = i.idSeveridad
        WHERE i.activo = true AND i.fechaRegistro BETWEEN :inicio AND :fin GROUP BY s.nombre ORDER BY COUNT(i) DESC""")
    List<IndicadorAgrupadoResponse> contarPorSeveridad(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("""
        SELECT new com.proyecto.integrador.model.response.CargaResponsableResponse(
        CONCAT(CONCAT(u.nombres, ' '), u.apePaterno), COUNT(i))
        FROM IncidenciaEntity i JOIN UsuarioEntity u ON u.idUsuario = i.idResponsable
        WHERE i.activo = true AND i.idEstado NOT IN (6, 7, 8) GROUP BY u.nombres, u.apePaterno ORDER BY COUNT(i) DESC""")
    List<CargaResponsableResponse> cargaActivaResponsables();

    @Query("""
        SELECT COUNT(i) FROM IncidenciaEntity i WHERE i.activo = true AND i.fechaRegistro BETWEEN :inicio AND :fin
        """)
    Long totalPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("""
        SELECT COUNT(i) FROM IncidenciaEntity i WHERE i.activo = true AND i.fechaRegistro BETWEEN :inicio AND :fin
        AND ((i.fechaResolucion IS NOT NULL AND i.fechaResolucion <= i.fechaLimite) OR (i.fechaCierre IS NOT NULL AND i.fechaCierre <= i.fechaLimite))""")
    Long cumplimientoSla(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
