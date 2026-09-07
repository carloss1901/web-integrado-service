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
public interface IncidenciaAccionCorrectivaRepository extends JpaRepository<IncidenciaAccionCorrectivaEntity, Integer> {

    @Query(value = """
        SELECT
            A.ID_ACCION_CORRECTIVA AS idAccionCorrectiva,
            A.ID_INCIDENCIA AS idIncidencia,
            A.ID_USUARIO AS idUsuario,
            CONCAT(U.NOMBRES, ' ', U.APELLIDOS) AS usuario,
            R.NOMBRE AS rol,
            A.DESCRIPCION AS descripcion,
            FORMATDATETIME(A.FECHA_REGISTRO, 'dd/MM/yyyy HH:mm') AS fechaRegistro
        FROM INCIDENCIA_ACCION_CORRECTIVA A
        INNER JOIN USUARIO U ON U.ID_USUARIO = A.ID_USUARIO
        LEFT JOIN USUARIO_ROL UR ON UR.ID_USUARIO = U.ID_USUARIO
            AND UR.ACTIVO = TRUE AND UR.ESTADO = TRUE
        LEFT JOIN ROL R ON R.ID_ROL = UR.ID_ROL AND R.ACTIVO = TRUE
        WHERE A.ID_INCIDENCIA = :idIncidencia
        AND A.ACTIVO = TRUE
        ORDER BY A.FECHA_REGISTRO
        """,
        countQuery = """
        SELECT COUNT(1)
        FROM INCIDENCIA_ACCION_CORRECTIVA A
        WHERE A.ID_INCIDENCIA = :idIncidencia
        AND A.ACTIVO = TRUE
        """,
        nativeQuery = true)
    Page<IncidenciaAccionCorrectivaProjection> listarAccionesCorrectivas(@Param("idIncidencia") Integer idIncidencia,
                                                                         Pageable pageable);
}