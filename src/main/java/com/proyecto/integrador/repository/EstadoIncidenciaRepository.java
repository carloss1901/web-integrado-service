package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.EstadoIncidenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoIncidenciaRepository extends JpaRepository<EstadoIncidenciaEntity, Integer> {
}
