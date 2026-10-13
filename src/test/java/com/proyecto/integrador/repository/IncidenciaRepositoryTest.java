package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.proyecto.integrador.model.response.IncidenciaBusquedaResponse;
import com.proyecto.integrador.model.response.IndicadoresIncidenciaResponse;
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

    @Test
    void obtenerIndicadoresDebeCalcularDatosDesdePersistencia() {
        IncidenciaEntity pendienteVencida = crearIncidencia(
                "INC-IND-001",
                "Incidencia pendiente vencida",
                true
        );
        pendienteVencida.setIdEstado(1);
        pendienteVencida.setFechaLimite(
                LocalDateTime.now().minusHours(2)
        );

        IncidenciaEntity resuelta = crearIncidencia(
                "INC-IND-002",
                "Incidencia resuelta",
                true
        );
        resuelta.setIdEstado(6);

        IncidenciaEntity inactiva = crearIncidencia(
                "INC-IND-003",
                "Incidencia inactiva",
                false
        );

        incidenciaRepository.save(pendienteVencida);
        incidenciaRepository.save(resuelta);
        incidenciaRepository.save(inactiva);
        incidenciaRepository.flush();

        IndicadoresIncidenciaResponse resultado =
                incidenciaRepository.obtenerIndicadores();

        assertEquals(2L, resultado.getTotalIncidencias());
        assertEquals(1L, resultado.getIncidenciasPendientes());
        assertEquals(1L, resultado.getIncidenciasVencidas());
        assertEquals(1L, resultado.getIncidenciasResueltas());
        assertEquals(0L, resultado.getIncidenciasCerradas());
    }
}