package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaEvidenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenciaEvidenciaRepository extends JpaRepository<IncidenciaEvidenciaEntity, Integer> {
}
