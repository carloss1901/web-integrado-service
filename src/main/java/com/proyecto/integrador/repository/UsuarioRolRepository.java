package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Integer> {

    @Query(value = """
        SELECT ID_ROL
        FROM USUARIO_ROL
        WHERE ID_USUARIO = :idUsuario
        AND ACTIVO = TRUE
        AND ESTADO = TRUE
        ORDER BY ID_ROL
        """, nativeQuery = true)
    List<Integer> listarIdsRolesPorUsuario(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Query(value = "DELETE FROM USUARIO_ROL WHERE ID_USUARIO = :idUsuario", nativeQuery = true)
    void deleteByIdUsuario(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Query(value = """
        UPDATE USUARIO_ROL
        SET ACTIVO = FALSE,
            ESTADO = FALSE,
            FEC_MOD = CURRENT_TIMESTAMP
        WHERE ID_USUARIO = :idUsuario
        """, nativeQuery = true)
    void desactivarByIdUsuario(@Param("idUsuario") Integer idUsuario);
}
