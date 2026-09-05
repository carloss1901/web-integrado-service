package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.EstadoIncidenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoIncidenciaRepository extends JpaRepository<EstadoIncidenciaEntity, Integer> {

    @Query(value = """
        SELECT *
        FROM ESTADO_INCIDENCIA
        WHERE UPPER(NOMBRE) = UPPER(:nombre)
        AND ACTIVO = TRUE
        """, nativeQuery = true)
    Optional<EstadoIncidenciaEntity> findByNombreActivo(@Param("nombre") String nombre);
}
