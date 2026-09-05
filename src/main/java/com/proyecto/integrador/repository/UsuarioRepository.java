package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    Boolean existsByUsuarioIgnoreCase(String usuario);

    Boolean existsByCorreoIgnoreCase(String correo);

    @Query(value = """
        SELECT COUNT(1)
        FROM USUARIO U
        INNER JOIN USUARIO_ROL UR ON UR.ID_USUARIO = U.ID_USUARIO
        INNER JOIN ROL R ON R.ID_ROL = UR.ID_ROL
        WHERE U.ID_USUARIO = :idUsuario
        AND U.ESTADO = TRUE
        AND UR.ESTADO = TRUE
        AND UPPER(R.NOMBRE) = UPPER(:nombreRol)
        """, nativeQuery = true)
    Integer countUsuarioByRol(@Param("idUsuario") Integer idUsuario, @Param("nombreRol") String nombreRol);
}
