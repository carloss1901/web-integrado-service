package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.EstadoIncidenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoIncidenciaRepository extends JpaRepository<EstadoIncidenciaEntity, Integer> {
    Optional<EstadoIncidenciaEntity> findByNombreIgnoreCase(String nombre);
}
