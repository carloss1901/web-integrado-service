package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.projection.MaestroProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComunesRepository extends JpaRepository<RolEntity, Integer> {

    @Query(value = """
        SELECT
            ID_ROL AS maestroId,
            NOMBRE AS descripcion
        FROM ROL
        WHERE ACTIVO = TRUE
        ORDER BY NOMBRE
        """, nativeQuery = true)
    List<MaestroProjection> listarRoles();

    @Query(value = """
        SELECT
            ID_CATEGORIA AS maestroId,
            NOMBRE AS descripcion
        FROM CATEGORIA
        WHERE ACTIVO = TRUE
        ORDER BY NOMBRE
        """, nativeQuery = true)
    List<MaestroProjection> listarCategorias();

    @Query(value = """
        SELECT
            ID_UBICACION AS maestroId,
            NOMBRE AS descripcion
        FROM UBICACION
        WHERE ACTIVO = TRUE
        ORDER BY NOMBRE
        """, nativeQuery = true)
    List<MaestroProjection> listarUbicaciones();

    @Query(value = """
        SELECT
            ID_SEVERIDAD AS maestroId,
            NOMBRE AS descripcion
        FROM SEVERIDAD
        WHERE ACTIVO = TRUE
        ORDER BY NIVEL
        """, nativeQuery = true)
    List<MaestroProjection> listarSeveridades();

    @Query(value = """
        SELECT
            ID_PRIORIDAD AS maestroId,
            NOMBRE AS descripcion
        FROM PRIORIDAD
        WHERE ACTIVO = TRUE
        ORDER BY NIVEL
        """, nativeQuery = true)
    List<MaestroProjection> listarPrioridades();

    @Query(value = """
        SELECT
            ID_ESTADO AS maestroId,
            NOMBRE AS descripcion
        FROM ESTADO_INCIDENCIA
        WHERE ACTIVO = TRUE
        ORDER BY ID_ESTADO
        """, nativeQuery = true)
    List<MaestroProjection> listarEstadosIncidencia();
}
