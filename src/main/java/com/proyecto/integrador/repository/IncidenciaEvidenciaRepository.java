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
public interface IncidenciaEvidenciaRepository extends JpaRepository<IncidenciaEvidenciaEntity, Integer> {

    @Query(value = """
        SELECT
            E.ID_EVIDENCIA AS idEvidencia,
            E.ID_INCIDENCIA AS idIncidencia,
            E.ID_USUARIO AS idUsuario,
            CONCAT(U.NOMBRES, ' ', U.APELLIDOS) AS usuario,
            R.NOMBRE AS rol,
            E.NOMBRE_ARCHIVO AS nombreArchivo,
            E.RUTA_ARCHIVO AS rutaArchivo,
            E.DESCRIPCION AS descripcion,
            FORMATDATETIME(E.FECHA_REGISTRO, 'dd/MM/yyyy HH:mm') AS fechaRegistro
        FROM INCIDENCIA_EVIDENCIA E
        INNER JOIN USUARIO U ON U.ID_USUARIO = E.ID_USUARIO
        LEFT JOIN USUARIO_ROL UR ON UR.ID_USUARIO = U.ID_USUARIO
            AND UR.ACTIVO = TRUE AND UR.ESTADO = TRUE
        LEFT JOIN ROL R ON R.ID_ROL = UR.ID_ROL AND R.ACTIVO = TRUE
        WHERE E.ID_INCIDENCIA = :idIncidencia
        AND E.ACTIVO = TRUE
        ORDER BY E.FECHA_REGISTRO
        """,
        countQuery = """
        SELECT COUNT(1)
        FROM INCIDENCIA_EVIDENCIA E
        WHERE E.ID_INCIDENCIA = :idIncidencia
        AND E.ACTIVO = TRUE
        """,
        nativeQuery = true)
    Page<IncidenciaEvidenciaProjection> listarEvidencias(@Param("idIncidencia") Integer idIncidencia, Pageable pageable);
}
