package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaHistorialEntity;
import com.proyecto.integrador.model.projection.IncidenciaHistorialProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaHistorialRepository extends JpaRepository<IncidenciaHistorialEntity, Integer> {

    @Query(value = """
        SELECT
            H.ID_HISTORIAL AS idHistorial,
            H.ID_INCIDENCIA AS idIncidencia,
            H.ID_USUARIO AS idUsuario,
            CONCAT(U.NOMBRES, ' ', U.APELLIDOS) AS usuario,
            R.NOMBRE AS rol,
            H.TIPO_EVENTO AS tipoEvento,
            H.VALOR_ANTERIOR AS valorAnterior,
            H.VALOR_NUEVO AS valorNuevo,
            H.DESCRIPCION AS descripcion,
            FORMATDATETIME(H.FECHA_EVENTO, 'dd/MM/yyyy HH:mm') AS fechaEvento
        FROM INCIDENCIA_HISTORIAL H
        INNER JOIN USUARIO U ON U.ID_USUARIO = H.ID_USUARIO
        LEFT JOIN USUARIO_ROL UR ON UR.ID_USUARIO = U.ID_USUARIO
            AND UR.ACTIVO = TRUE AND UR.ESTADO = TRUE
        LEFT JOIN ROL R ON R.ID_ROL = UR.ID_ROL AND R.ACTIVO = TRUE
        WHERE H.ID_INCIDENCIA = :idIncidencia
        AND H.ACTIVO = TRUE
        ORDER BY H.FECHA_EVENTO
        """,
        countQuery = """
        SELECT COUNT(1)
        FROM INCIDENCIA_HISTORIAL H
        WHERE H.ID_INCIDENCIA = :idIncidencia
        AND H.ACTIVO = TRUE
        """,
        nativeQuery = true)
    Page<IncidenciaHistorialProjection> listarHistorial(@Param("idIncidencia") Integer idIncidencia, Pageable pageable);
}
