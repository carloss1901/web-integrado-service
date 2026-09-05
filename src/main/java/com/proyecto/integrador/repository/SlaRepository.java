package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.SlaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlaRepository extends JpaRepository<SlaEntity, Integer> {
    Optional<SlaEntity> findByIdSeveridad(Integer idSeveridad);
}
