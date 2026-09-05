package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.projection.UsuarioProjection;
import com.proyecto.integrador.model.request.usuario.ListarUsuarioRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    @Query(value = """
        SELECT
            U.ID_USUARIO AS idUsuario,
            U.USUARIO AS usuario,
            U.NOMBRES AS nombres,
            U.APELLIDOS AS apellidos,
            U.CORREO AS correo,
            U.ACTIVO AS activo,
            CASE WHEN U.ACTIVO = TRUE THEN 'ACTIVO' ELSE 'INACTIVO' END AS estadoDsc,
            COALESCE(GROUP_CONCAT(R.NOMBRE ORDER BY R.NOMBRE SEPARATOR ', '), '') AS roles,
            FORMATDATETIME(U.FEC_REG, 'dd/MM/yyyy HH:mm') AS fecRegistro,
            FORMATDATETIME(U.FEC_MOD, 'dd/MM/yyyy HH:mm') AS fecModificacion
        FROM USUARIO U
        LEFT JOIN USUARIO_ROL UR ON UR.ID_USUARIO = U.ID_USUARIO
            AND UR.ACTIVO = TRUE
            AND UR.ESTADO = TRUE
        LEFT JOIN ROL R ON R.ID_ROL = UR.ID_ROL
            AND R.ACTIVO = TRUE
        WHERE (:#{#rq.usuario} IS NULL OR :#{#rq.usuario} = '' OR LOWER(U.USUARIO)
            LIKE LOWER(CONCAT('%', :#{#rq.usuario}, '%')))
        AND (:#{#rq.nombres} IS NULL OR :#{#rq.nombres} = '' OR LOWER(U.NOMBRES)
            LIKE LOWER(CONCAT('%', :#{#rq.nombres}, '%')))
        AND (:#{#rq.apellidos} IS NULL OR :#{#rq.apellidos} = '' OR LOWER(U.APELLIDOS)
            LIKE LOWER(CONCAT('%', :#{#rq.apellidos}, '%')))
        AND (:#{#rq.correo} IS NULL OR :#{#rq.correo} = '' OR LOWER(U.CORREO)
            LIKE LOWER(CONCAT('%', :#{#rq.correo}, '%')))
        AND (:#{#rq.estado} IS NULL OR U.ACTIVO = :#{#rq.estado})
        GROUP BY U.ID_USUARIO, U.USUARIO, U.NOMBRES, U.APELLIDOS, U.CORREO, U.ACTIVO, U.FEC_REG, U.FEC_MOD
        ORDER BY U.ID_USUARIO DESC
        """,
        countQuery = """
        SELECT COUNT(1)
        FROM USUARIO U
        WHERE (:#{#rq.usuario} IS NULL OR :#{#rq.usuario} = '' OR LOWER(U.USUARIO)
            LIKE LOWER(CONCAT('%', :#{#rq.usuario}, '%')))
        AND (:#{#rq.nombres} IS NULL OR :#{#rq.nombres} = '' OR LOWER(U.NOMBRES)
            LIKE LOWER(CONCAT('%', :#{#rq.nombres}, '%')))
        AND (:#{#rq.apellidos} IS NULL OR :#{#rq.apellidos} = '' OR LOWER(U.APELLIDOS)
            LIKE LOWER(CONCAT('%', :#{#rq.apellidos}, '%')))
        AND (:#{#rq.correo} IS NULL OR :#{#rq.correo} = '' OR LOWER(U.CORREO)
            LIKE LOWER(CONCAT('%', :#{#rq.correo}, '%')))
        AND (:#{#rq.estado} IS NULL OR U.ACTIVO = :#{#rq.estado})
        """,
        nativeQuery = true)
    Page<UsuarioProjection> listarUsuario(
        @Param("rq") ListarUsuarioRequest rq,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    Boolean existsByUsuarioIgnoreCase(String usuario);

    Boolean existsByCorreoIgnoreCase(String correo);

    @Query(value = """
        SELECT COUNT(1)
        FROM USUARIO
        WHERE UPPER(USUARIO) = UPPER(:usuario)
        AND ID_USUARIO <> :idUsuario
        """, nativeQuery = true)
    Integer countByUsuarioAndIdUsuarioNot(@Param("usuario") String usuario, @Param("idUsuario") Integer idUsuario);

    @Query(value = """
        SELECT COUNT(1)
        FROM USUARIO
        WHERE UPPER(CORREO) = UPPER(:correo)
        AND ID_USUARIO <> :idUsuario
        """, nativeQuery = true)
    Integer countByCorreoAndIdUsuarioNot(@Param("correo") String correo, @Param("idUsuario") Integer idUsuario);

    @Query(value = """
        SELECT COUNT(1)
        FROM USUARIO U
        INNER JOIN USUARIO_ROL UR ON UR.ID_USUARIO = U.ID_USUARIO
        INNER JOIN ROL R ON R.ID_ROL = UR.ID_ROL
        WHERE U.ID_USUARIO = :idUsuario
        AND U.ESTADO = TRUE
        AND U.ACTIVO = TRUE
        AND UR.ESTADO = TRUE
        AND UR.ACTIVO = TRUE
        AND R.ACTIVO = TRUE
        AND UPPER(R.NOMBRE) = UPPER(:nombreRol)
        """, nativeQuery = true)
    Integer countUsuarioByRol(@Param("idUsuario") Integer idUsuario, @Param("nombreRol") String nombreRol);
}
