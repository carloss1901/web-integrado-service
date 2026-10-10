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
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Repository
public interface IncidenciaRepository extends JpaRepository<IncidenciaEntity, Integer> {

    @Query(value = """
        SELECT
            I.ID_INCIDENCIA AS idIncidencia,
            I.CODIGO AS codigo,
            CONCAT(UR.NOMBRES, ' ', UR.APELLIDOS) AS reportante,
            CONCAT(UA.NOMBRES, ' ', UA.APELLIDOS) AS responsable,
            C.NOMBRE AS categoria,
            U.NOMBRE AS ubicacion,
            S.NOMBRE AS severidad,
            P.NOMBRE AS prioridad,
            E.NOMBRE AS estado,
            I.PUNTAJE_PRIORIDAD AS puntajePrioridad,
            I.TITULO AS titulo,
            FORMATDATETIME(I.FECHA_REGISTRO, 'dd/MM/yyyy HH:mm') AS fechaRegistro,
            FORMATDATETIME(I.FECHA_LIMITE, 'dd/MM/yyyy HH:mm') AS fechaLimite,
            FORMATDATETIME(I.FECHA_RESOLUCION, 'dd/MM/yyyy HH:mm') AS fechaResolucion,
            FORMATDATETIME(I.FECHA_CIERRE, 'dd/MM/yyyy HH:mm') AS fechaCierre
        FROM INCIDENCIA I
        INNER JOIN USUARIO UR ON UR.ID_USUARIO = I.ID_REPORTANTE
        LEFT JOIN USUARIO UA ON UA.ID_USUARIO = I.ID_RESPONSABLE
        INNER JOIN CATEGORIA C ON C.ID_CATEGORIA = I.ID_CATEGORIA
        INNER JOIN UBICACION U ON U.ID_UBICACION = I.ID_UBICACION
        LEFT JOIN SEVERIDAD S ON S.ID_SEVERIDAD = I.ID_SEVERIDAD
        LEFT JOIN PRIORIDAD P ON P.ID_PRIORIDAD = I.ID_PRIORIDAD
        INNER JOIN ESTADO_INCIDENCIA E ON E.ID_ESTADO = I.ID_ESTADO
        WHERE I.ACTIVO = TRUE
          AND I.FECHA_LIMITE IS NOT NULL
          AND I.FECHA_LIMITE < CURRENT_TIMESTAMP
          AND I.ID_ESTADO NOT IN (6, 7, 8)
        ORDER BY I.FECHA_LIMITE
        """,
        countQuery = """
        SELECT COUNT(1)
        FROM INCIDENCIA I
        WHERE I.ACTIVO = TRUE
          AND I.FECHA_LIMITE IS NOT NULL
          AND I.FECHA_LIMITE < CURRENT_TIMESTAMP
          AND I.ID_ESTADO NOT IN (6, 7, 8)
        """,
        nativeQuery = true)
    Page<IncidenciaProjection> listarIncidenciasVencidas(Pageable pageable);

    @Query(value = """
        SELECT
            I.ID_INCIDENCIA AS idIncidencia,
            I.CODIGO AS codigo,
            CONCAT(UR.NOMBRES, ' ', UR.APELLIDOS) AS reportante,
            CONCAT(UA.NOMBRES, ' ', UA.APELLIDOS) AS responsable,
            C.NOMBRE AS categoria,
            U.NOMBRE AS ubicacion,
            S.NOMBRE AS severidad,
            P.NOMBRE AS prioridad,
            E.NOMBRE AS estado,
            I.PUNTAJE_PRIORIDAD AS puntajePrioridad,
            I.TITULO AS titulo,
            FORMATDATETIME(I.FECHA_REGISTRO, 'dd/MM/yyyy HH:mm') AS fechaRegistro,
            FORMATDATETIME(I.FECHA_LIMITE, 'dd/MM/yyyy HH:mm') AS fechaLimite,
            FORMATDATETIME(I.FECHA_RESOLUCION, 'dd/MM/yyyy HH:mm') AS fechaResolucion,
            FORMATDATETIME(I.FECHA_CIERRE, 'dd/MM/yyyy HH:mm') AS fechaCierre
        FROM INCIDENCIA I
        INNER JOIN USUARIO UR ON UR.ID_USUARIO = I.ID_REPORTANTE
        LEFT JOIN USUARIO UA ON UA.ID_USUARIO = I.ID_RESPONSABLE
        INNER JOIN CATEGORIA C ON C.ID_CATEGORIA = I.ID_CATEGORIA
        INNER JOIN UBICACION U ON U.ID_UBICACION = I.ID_UBICACION
        LEFT JOIN SEVERIDAD S ON S.ID_SEVERIDAD = I.ID_SEVERIDAD
        LEFT JOIN PRIORIDAD P ON P.ID_PRIORIDAD = I.ID_PRIORIDAD
        INNER JOIN ESTADO_INCIDENCIA E ON E.ID_ESTADO = I.ID_ESTADO
        WHERE I.ACTIVO = TRUE
        ORDER BY I.FECHA_REGISTRO DESC
        """, nativeQuery = true)
    List<IncidenciaProjection> listarIncidencias();

    @Query(value = """
        SELECT
            I.ID_INCIDENCIA AS idIncidencia,
            I.CODIGO AS codigo,
            CONCAT(UR.NOMBRES, ' ', UR.APELLIDOS) AS reportante,
            CONCAT(UA.NOMBRES, ' ', UA.APELLIDOS) AS responsable,
            C.NOMBRE AS categoria,
            U.NOMBRE AS ubicacion,
            S.NOMBRE AS severidad,
            P.NOMBRE AS prioridad,
            E.NOMBRE AS estado,
            I.PUNTAJE_PRIORIDAD AS puntajePrioridad,
            I.TITULO AS titulo,
            FORMATDATETIME(I.FECHA_REGISTRO, 'dd/MM/yyyy HH:mm') AS fechaRegistro,
            FORMATDATETIME(I.FECHA_LIMITE, 'dd/MM/yyyy HH:mm') AS fechaLimite,
            FORMATDATETIME(I.FECHA_RESOLUCION, 'dd/MM/yyyy HH:mm') AS fechaResolucion,
            FORMATDATETIME(I.FECHA_CIERRE, 'dd/MM/yyyy HH:mm') AS fechaCierre
        FROM INCIDENCIA I
        INNER JOIN USUARIO UR ON UR.ID_USUARIO = I.ID_REPORTANTE
        LEFT JOIN USUARIO UA ON UA.ID_USUARIO = I.ID_RESPONSABLE
        INNER JOIN CATEGORIA C ON C.ID_CATEGORIA = I.ID_CATEGORIA
        INNER JOIN UBICACION U ON U.ID_UBICACION = I.ID_UBICACION
        LEFT JOIN SEVERIDAD S ON S.ID_SEVERIDAD = I.ID_SEVERIDAD
        LEFT JOIN PRIORIDAD P ON P.ID_PRIORIDAD = I.ID_PRIORIDAD
        INNER JOIN ESTADO_INCIDENCIA E ON E.ID_ESTADO = I.ID_ESTADO
        WHERE I.ACTIVO = TRUE
          AND I.ID_INCIDENCIA = :idIncidencia
        """, nativeQuery = true)
    Optional<IncidenciaProjection> obtenerIncidencia(Integer idIncidencia);

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
}
