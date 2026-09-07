package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaComentarioEntity;
import com.proyecto.integrador.model.projection.IncidenciaComentarioProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenciaComentarioRepository extends JpaRepository<IncidenciaComentarioEntity, Integer> {

    @Query(value = """
        SELECT
            C.ID_COMENTARIO AS idComentario,
            C.ID_INCIDENCIA AS idIncidencia,
            C.ID_USUARIO AS idUsuario,
            CONCAT(U.NOMBRES, ' ', U.APELLIDOS) AS usuario,
            R.NOMBRE AS rol,
            C.COMENTARIO AS comentario,
            FORMATDATETIME(C.FECHA_REGISTRO, 'dd/MM/yyyy HH:mm') AS fechaRegistro
        FROM INCIDENCIA_COMENTARIO C
        INNER JOIN USUARIO U ON U.ID_USUARIO = C.ID_USUARIO
        LEFT JOIN USUARIO_ROL UR ON UR.ID_USUARIO = U.ID_USUARIO
            AND UR.ACTIVO = TRUE AND UR.ESTADO = TRUE
        LEFT JOIN ROL R ON R.ID_ROL = UR.ID_ROL AND R.ACTIVO = TRUE
        WHERE C.ID_INCIDENCIA = :idIncidencia
        AND C.ACTIVO = TRUE
        ORDER BY C.FECHA_REGISTRO
        """,
        countQuery = """
        SELECT COUNT(1)
        FROM INCIDENCIA_COMENTARIO C
        WHERE C.ID_INCIDENCIA = :idIncidencia
        AND C.ACTIVO = TRUE
        """,
        nativeQuery = true)
    Page<IncidenciaComentarioProjection> listarComentarios(@Param("idIncidencia") Integer idIncidencia,
                                                           Pageable pageable);
}