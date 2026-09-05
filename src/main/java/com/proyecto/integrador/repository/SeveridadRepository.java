package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.SeveridadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeveridadRepository extends JpaRepository<SeveridadEntity, Integer> {
}
