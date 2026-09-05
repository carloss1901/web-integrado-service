package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.PrioridadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrioridadRepository extends JpaRepository<PrioridadEntity, Integer> {

    @Query(value = """
        SELECT *
        FROM PRIORIDAD
        WHERE :puntaje BETWEEN PUNTAJE_MINIMO AND PUNTAJE_MAXIMO
        AND ACTIVO = TRUE
        ORDER BY NIVEL DESC
        LIMIT 1
        """, nativeQuery = true)
    Optional<PrioridadEntity> findByPuntaje(@Param("puntaje") Integer puntaje);
}
