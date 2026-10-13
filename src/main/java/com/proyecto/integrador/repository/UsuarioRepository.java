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

import java.util.Optional;

@Repository
public interface UsuarioRepository
        extends JpaRepository<UsuarioEntity, Integer> {

    Optional<UsuarioEntity> findByUsuarioIgnoreCase(String usuario);

    @Query(
            value = """
            SELECT
                u.idUsuario AS idUsuario,
                u.usuario AS usuario,
                u.nombres AS nombres,
                u.apellidos AS apellidos,
                u.correo AS correo,
                u.activo AS activo,
                CASE
                    WHEN u.activo = true THEN 'ACTIVO'
                    ELSE 'INACTIVO'
                END AS estadoDsc,
                COALESCE(
                    FUNCTION('STRING_AGG', r.nombre, ', '),
                    ''
                ) AS roles,
                CAST(u.fecReg AS string) AS fecRegistro,
                CAST(u.fecMod AS string) AS fecModificacion
            FROM UsuarioEntity u
            LEFT JOIN UsuarioRolEntity ur
                ON ur.idUsuario = u.idUsuario
                AND ur.activo = true
                AND ur.estado = true
            LEFT JOIN RolEntity r
                ON r.idRol = ur.idRol
                AND r.activo = true
            WHERE (
                :#{#rq.usuario} IS NULL
                OR :#{#rq.usuario} = ''
                OR LOWER(u.usuario) LIKE LOWER(
                    CONCAT('%', CONCAT(:#{#rq.usuario}, '%'))
                )
            )
            AND (
                :#{#rq.nombres} IS NULL
                OR :#{#rq.nombres} = ''
                OR LOWER(u.nombres) LIKE LOWER(
                    CONCAT('%', CONCAT(:#{#rq.nombres}, '%'))
                )
            )
            AND (
                :#{#rq.apellidos} IS NULL
                OR :#{#rq.apellidos} = ''
                OR LOWER(u.apellidos) LIKE LOWER(
                    CONCAT('%', CONCAT(:#{#rq.apellidos}, '%'))
                )
            )
            AND (
                :#{#rq.correo} IS NULL
                OR :#{#rq.correo} = ''
                OR LOWER(u.correo) LIKE LOWER(
                    CONCAT('%', CONCAT(:#{#rq.correo}, '%'))
                )
            )
            AND (
                :#{#rq.estado} IS NULL
                OR u.activo = :#{#rq.estado}
            )
            GROUP BY
                u.idUsuario,
                u.usuario,
                u.nombres,
                u.apellidos,
                u.correo,
                u.activo,
                u.fecReg,
                u.fecMod
            ORDER BY u.idUsuario DESC
            """,
            countQuery = """
            SELECT COUNT(u)
            FROM UsuarioEntity u
            WHERE (
                :#{#rq.usuario} IS NULL
                OR :#{#rq.usuario} = ''
                OR LOWER(u.usuario) LIKE LOWER(
                    CONCAT('%', CONCAT(:#{#rq.usuario}, '%'))
                )
            )
            AND (
                :#{#rq.nombres} IS NULL
                OR :#{#rq.nombres} = ''
                OR LOWER(u.nombres) LIKE LOWER(
                    CONCAT('%', CONCAT(:#{#rq.nombres}, '%'))
                )
            )
            AND (
                :#{#rq.apellidos} IS NULL
                OR :#{#rq.apellidos} = ''
                OR LOWER(u.apellidos) LIKE LOWER(
                    CONCAT('%', CONCAT(:#{#rq.apellidos}, '%'))
                )
            )
            AND (
                :#{#rq.correo} IS NULL
                OR :#{#rq.correo} = ''
                OR LOWER(u.correo) LIKE LOWER(
                    CONCAT('%', CONCAT(:#{#rq.correo}, '%'))
                )
            )
            AND (
                :#{#rq.estado} IS NULL
                OR u.activo = :#{#rq.estado}
            )
            """
    )
    Page<UsuarioProjection> listarUsuario(
            @Param("rq") ListarUsuarioRequest rq,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    Boolean existsByUsuarioIgnoreCase(String usuario);

    Boolean existsByCorreoIgnoreCase(String correo);

    @Query("""
        SELECT COUNT(u)
        FROM UsuarioEntity u
        WHERE UPPER(u.usuario) = UPPER(:usuario)
          AND u.idUsuario <> :idUsuario
        """)
    Integer countByUsuarioAndIdUsuarioNot(
            @Param("usuario") String usuario,
            @Param("idUsuario") Integer idUsuario
    );

    @Query("""
        SELECT COUNT(u)
        FROM UsuarioEntity u
        WHERE UPPER(u.correo) = UPPER(:correo)
          AND u.idUsuario <> :idUsuario
        """)
    Integer countByCorreoAndIdUsuarioNot(
            @Param("correo") String correo,
            @Param("idUsuario") Integer idUsuario
    );

    @Query("""
        SELECT COUNT(ur)
        FROM UsuarioEntity u
        JOIN UsuarioRolEntity ur
          ON ur.idUsuario = u.idUsuario
        JOIN RolEntity r
          ON r.idRol = ur.idRol
        WHERE u.idUsuario = :idUsuario
          AND u.estado = true
          AND u.activo = true
          AND ur.estado = true
          AND ur.activo = true
          AND r.activo = true
          AND r.idRol = :idRol
        """)
    Integer countUsuarioByRolId(
            @Param("idUsuario") Integer idUsuario,
            @Param("idRol") Integer idRol
    );
}