package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.SlaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlaRepository
        extends JpaRepository<SlaEntity, Integer> {

    @Query("""
        SELECT s
        FROM SlaEntity s
        WHERE s.idSeveridad = :idSeveridad
          AND s.activo = true
        """)
    Optional<SlaEntity> findActivoByIdSeveridad(
            @Param("idSeveridad") Integer idSeveridad
    );
}