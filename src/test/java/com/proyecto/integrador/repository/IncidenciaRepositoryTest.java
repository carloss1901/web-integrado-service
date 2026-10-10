package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.proyecto.integrador.model.response.IncidenciaBusquedaResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class IncidenciaRepositoryTest {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    @Test
    void buscarPorTituloDebeEjecutarConsultaJpql() {
        IncidenciaEntity activa = crearIncidencia(
                "INC-JPQL-001",
                "Equipo no enciende",
                true
        );

        IncidenciaEntity inactiva = crearIncidencia(
                "INC-JPQL-002",
                "Equipo antiguo",
                false
        );

        incidenciaRepository.save(activa);
        incidenciaRepository.save(inactiva);
        incidenciaRepository.flush();

        List<IncidenciaBusquedaResponse> resultado =
                incidenciaRepository.buscarResumenPorTitulo(
                        "EQUIPO"
                );

        assertEquals(1, resultado.size());
        assertEquals(
                "Equipo no enciende",
                resultado.get(0).getTitulo()
        );
    }

    private IncidenciaEntity crearIncidencia(
            String codigo,
            String titulo,
            boolean activo
    ) {
        IncidenciaEntity incidencia = new IncidenciaEntity();

        incidencia.setCodigo(codigo);
        incidencia.setIdReportante(1);
        incidencia.setIdCategoria(1);
        incidencia.setIdUbicacion(1);
        incidencia.setIdEstado(1);
        incidencia.setTitulo(titulo);
        incidencia.setDescripcion("Incidencia para prueba JPQL");
        incidencia.setFechaRegistro(LocalDateTime.now());
        incidencia.setActivo(activo);

        return incidencia;
    }
}