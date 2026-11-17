package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.entity.IncidenciaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;
import com.proyecto.integrador.model.response.IncidenciaBusquedaResponse;
import com.proyecto.integrador.model.response.IndicadoresIncidenciaResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.datasource.url=jdbc:h2:mem:incidencias;MODE=MSSQLServer;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
@AutoConfigureTestDatabase(replace = Replace.NONE)
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
        IncidenciaEntity incidenciaDeOtroReportante = crearIncidencia(
                "INC-JPQL-003",
                "Equipo de otro reportante",
                true
        );
        incidenciaDeOtroReportante.setIdReportante(2);

        incidenciaRepository.save(activa);
        incidenciaRepository.save(inactiva);
        incidenciaRepository.save(incidenciaDeOtroReportante);
        incidenciaRepository.flush();

        List<IncidenciaBusquedaResponse> resultado =
                incidenciaRepository.buscarResumenPorTitulo(
                        "EQUIPO",
                        1
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
