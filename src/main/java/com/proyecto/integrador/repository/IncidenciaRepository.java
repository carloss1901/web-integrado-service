package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaEntity;
import com.proyecto.integrador.model.projection.IncidenciaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
}
