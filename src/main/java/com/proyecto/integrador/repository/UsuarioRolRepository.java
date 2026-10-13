package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRolRepository
        extends JpaRepository<UsuarioRolEntity, Integer> {

    @Query("""
        SELECT ur.idRol
        FROM UsuarioRolEntity ur
        WHERE ur.idUsuario = :idUsuario
          AND ur.activo = true
          AND ur.estado = true
        ORDER BY ur.idRol
        """)
    List<Integer> listarIdsRolesPorUsuario(
            @Param("idUsuario") Integer idUsuario
    );

    @Modifying
    @Query("""
        DELETE FROM UsuarioRolEntity ur
        WHERE ur.idUsuario = :idUsuario
        """)
    void deleteByIdUsuario(
            @Param("idUsuario") Integer idUsuario
    );

    @Modifying
    @Query("""
        UPDATE UsuarioRolEntity ur
        SET ur.activo = false,
            ur.estado = false,
            ur.fecMod = CURRENT_TIMESTAMP
        WHERE ur.idUsuario = :idUsuario
        """)
    void desactivarByIdUsuario(
            @Param("idUsuario") Integer idUsuario
    );
}