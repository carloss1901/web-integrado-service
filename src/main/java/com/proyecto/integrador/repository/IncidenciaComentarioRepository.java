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
public interface IncidenciaComentarioRepository
        extends JpaRepository<IncidenciaComentarioEntity, Integer> {

    @Query(
            value = """
            SELECT
                c.idComentario AS idComentario,
                c.idIncidencia AS idIncidencia,
                c.idUsuario AS idUsuario,
                CONCAT(CONCAT(u.nombres, ' '), CONCAT(u.apePaterno, CONCAT(' ', COALESCE(u.apeMaterno, '')))) AS usuario,
                c.comentario AS comentario,
                CAST(c.fechaRegistro AS string) AS fechaRegistro
            FROM IncidenciaComentarioEntity c
            JOIN UsuarioEntity u
              ON u.idUsuario = c.idUsuario
            WHERE c.idIncidencia = :idIncidencia
              AND c.activo = true
            ORDER BY c.fechaRegistro
            """,
            countQuery = """
            SELECT COUNT(c)
            FROM IncidenciaComentarioEntity c
            WHERE c.idIncidencia = :idIncidencia
              AND c.activo = true
            """
    )
    Page<IncidenciaComentarioProjection> listarComentarios(
            @Param("idIncidencia") Integer idIncidencia,
            Pageable pageable
    );
}
