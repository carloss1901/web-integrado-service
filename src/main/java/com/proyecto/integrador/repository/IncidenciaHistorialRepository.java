package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaHistorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenciaHistorialRepository extends JpaRepository<IncidenciaHistorialEntity, Integer> {
}
