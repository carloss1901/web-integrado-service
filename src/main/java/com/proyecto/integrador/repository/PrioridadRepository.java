package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.PrioridadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrioridadRepository
        extends JpaRepository<PrioridadEntity, Integer> {

    @Query("""
        SELECT p
        FROM PrioridadEntity p
        WHERE :puntaje BETWEEN
            p.puntajeMinimo AND p.puntajeMaximo
          AND p.activo = true
        ORDER BY p.nivel DESC
        """)
    Optional<PrioridadEntity> findByPuntaje(
            @Param("puntaje") Integer puntaje
    );
}