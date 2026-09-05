package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Integer> {
}
