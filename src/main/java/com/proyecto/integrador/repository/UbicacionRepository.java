package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UbicacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionEntity, Integer> {
}
