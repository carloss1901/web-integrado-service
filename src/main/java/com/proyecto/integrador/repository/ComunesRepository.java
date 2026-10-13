package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.projection.MaestroProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComunesRepository extends JpaRepository<RolEntity, Integer> {

    @Query("""
        SELECT r.idRol AS maestroId,
               r.nombre AS descripcion
        FROM RolEntity r
        WHERE r.activo = true
        ORDER BY r.nombre
        """)
    List<MaestroProjection> listarRoles();

    @Query("""
        SELECT c.idCategoria AS maestroId,
               c.nombre AS descripcion
        FROM CategoriaEntity c
        WHERE c.activo = true
        ORDER BY c.nombre
        """)
    List<MaestroProjection> listarCategorias();

    @Query("""
        SELECT u.idUbicacion AS maestroId,
               u.nombre AS descripcion
        FROM UbicacionEntity u
        WHERE u.activo = true
        ORDER BY u.nombre
        """)
    List<MaestroProjection> listarUbicaciones();

    @Query("""
        SELECT s.idSeveridad AS maestroId,
               s.nombre AS descripcion
        FROM SeveridadEntity s
        WHERE s.activo = true
        ORDER BY s.nivel
        """)
    List<MaestroProjection> listarSeveridades();

    @Query("""
        SELECT p.idPrioridad AS maestroId,
               p.nombre AS descripcion
        FROM PrioridadEntity p
        WHERE p.activo = true
        ORDER BY p.nivel
        """)
    List<MaestroProjection> listarPrioridades();

    @Query("""
        SELECT e.idEstado AS maestroId,
               e.nombre AS descripcion
        FROM EstadoIncidenciaEntity e
        WHERE e.activo = true
        ORDER BY e.idEstado
        """)
    List<MaestroProjection> listarEstadosIncidencia();

    @Query("""
        SELECT s.idSla AS maestroId,
               CONCAT(
                   sev.nombre,
                   ' - ',
                   CAST(s.minutosResolucion AS string),
                   ' min'
               ) AS descripcion
        FROM SlaEntity s
        JOIN SeveridadEntity sev
          ON sev.idSeveridad = s.idSeveridad
        WHERE s.activo = true
        ORDER BY s.idSla
        """)
    List<MaestroProjection> listarSla();
}