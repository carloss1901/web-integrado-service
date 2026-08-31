package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.ClienteEntity;
import com.proyecto.integrador.model.projection.ClienteProjection;
import com.proyecto.integrador.model.request.cliente.ListarClienteRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {

    @Query(value = """
        SELECT
            ID_CLIENTE	AS idCliente,
            NOMBRE,
            COD_EXTERNO	AS codExterno,
            DIRECCION,
            CORREO_ELECTRONICO	AS correo,
            ACTIVO,
            CASE WHEN ACTIVO = 1 THEN 'ACTIVO' ELSE 'INACTIVO'
            END AS estadoDsc,
            DATE_FORMAT(FECHA_CREACION, '%d/%m/%Y')	    AS fecCreacion,
            DATE_FORMAT(FECHA_MODIFICACION, '%d/%m/%Y') AS fecModificacion
        FROM CLIENTE
        WHERE (:#{#rq.nombre} IS NULL OR :#{#rq.nombre} = '' OR LOWER(NOMBRE)
            LIKE LOWER(CONCAT('%', :#{#rq.nombre}, '%')))
        AND (:#{#rq.codExterno} IS NULL OR :#{#rq.codExterno} = '' OR LOWER(COD_EXTERNO)
            LIKE LOWER(CONCAT('%', :#{#rq.codExterno}, '%')))
        AND (:#{#rq.estado} IS NULL OR ACTIVO = :#{#rq.estado})
        """, nativeQuery = true)
    Page<ClienteProjection> listarCliente(
        @Param("rq") ListarClienteRequest rq,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );
}
