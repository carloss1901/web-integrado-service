package com.proyecto.integrador.service;

import com.proyecto.integrador.mapper.GenericMapper;
import com.proyecto.integrador.model.entity.EstadoIncidenciaEntity;
import com.proyecto.integrador.model.entity.IncidenciaEntity;
import com.proyecto.integrador.model.entity.PrioridadEntity;
import com.proyecto.integrador.model.entity.SlaEntity;
import com.proyecto.integrador.model.projection.IncidenciaProjection;
import com.proyecto.integrador.model.request.incidencia.ActualizarEstadoIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.ActualizarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.AsignarResponsableRequest;
import com.proyecto.integrador.model.request.incidencia.ClasificarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarEvidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarIncidenciaRequest;
import com.proyecto.integrador.model.response.IncidenciaResponse;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.repository.EstadoIncidenciaRepository;
import com.proyecto.integrador.repository.IncidenciaEvidenciaRepository;
import com.proyecto.integrador.repository.IncidenciaHistorialRepository;
import com.proyecto.integrador.repository.IncidenciaRepository;
import com.proyecto.integrador.repository.PrioridadRepository;
import com.proyecto.integrador.repository.SeveridadRepository;
import com.proyecto.integrador.repository.SlaRepository;
import com.proyecto.integrador.repository.UbicacionRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.service.impl.IncidenciaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidenciaServiceImplTest {

    @Mock
    private IncidenciaRepository incidenciaRepository;

    @Mock
    private IncidenciaHistorialRepository incidenciaHistorialRepository;

    @Mock
    private IncidenciaEvidenciaRepository incidenciaEvidenciaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UbicacionRepository ubicacionRepository;

    @Mock
    private SeveridadRepository severidadRepository;

    @Mock
    private PrioridadRepository prioridadRepository;

    @Mock
    private SlaRepository slaRepository;

    @Mock
    private EstadoIncidenciaRepository estadoIncidenciaRepository;

    @Mock
    private GenericMapper genericMapper;

    @InjectMocks
    private IncidenciaServiceImpl incidenciaService;

    private IncidenciaEntity incidenciaActiva(Integer idIncidencia, Integer idResponsable) {
        IncidenciaEntity incidencia = new IncidenciaEntity();
        incidencia.setIdIncidencia(idIncidencia);
        incidencia.setIdReportante(1);
        incidencia.setIdResponsable(idResponsable);
        incidencia.setIdEstado(1);
        incidencia.setFechaRegistro(LocalDateTime.now());
        incidencia.setActivo(Boolean.TRUE);
        return incidencia;
    }

    private EstadoIncidenciaEntity estado(Integer id, String nombre) {
        EstadoIncidenciaEntity estado = new EstadoIncidenciaEntity();
        estado.setIdEstado(id);
        estado.setNombre(nombre);
        estado.setActivo(Boolean.TRUE);
        return estado;
    }

    @Test
    void registrarIncidenciaDebeResponderOk() {
        RegistrarIncidenciaRequest request = new RegistrarIncidenciaRequest();
        request.setIdReportante(1);
        request.setIdCategoria(1);
        request.setIdUbicacion(1);
        request.setTitulo("No enciende el equipo");
        request.setDescripcion("El equipo no muestra imagen");

        when(usuarioRepository.countUsuarioByRolId(1, 4)).thenReturn(1);
        when(categoriaRepository.existsById(1)).thenReturn(true);
        when(ubicacionRepository.existsById(1)).thenReturn(true);
        when(estadoIncidenciaRepository.findById(1))
            .thenReturn(Optional.of(estado(1, "REGISTRADA")));
        when(incidenciaRepository.save(any(IncidenciaEntity.class))).thenAnswer(invocation -> {
            IncidenciaEntity incidencia = invocation.getArgument(0);
            incidencia.setIdIncidencia(1);
            return incidencia;
        });

        ResponseEntity<Object> response = incidenciaService.registrarIncidencia(request);

        assertEquals(200, response.getStatusCode().value());
        verify(incidenciaRepository).save(any(IncidenciaEntity.class));
        verify(incidenciaHistorialRepository).save(any());
    }

    @Test
    void registrarIncidenciaDebeFallarSinRolReportante() {
        RegistrarIncidenciaRequest request = new RegistrarIncidenciaRequest();
        request.setIdReportante(1);

        when(usuarioRepository.countUsuarioByRolId(1, 4)).thenReturn(0);

        ResponseEntity<Object> response = incidenciaService.registrarIncidencia(request);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void actualizarIncidenciaDebeResponderOk() {
        ActualizarIncidenciaRequest request = new ActualizarIncidenciaRequest();
        request.setIdIncidencia(1);
        request.setIdCategoria(2);
        request.setIdUbicacion(2);
        request.setTitulo("Equipo reparado");
        request.setDescripcion("Actualizacion de detalle");

        when(categoriaRepository.existsById(2)).thenReturn(true);
        when(ubicacionRepository.existsById(2)).thenReturn(true);
        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidenciaActiva(1, null)));

        ResponseEntity<Object> response = incidenciaService.actualizarIncidencia(request);

        assertEquals(200, response.getStatusCode().value());
        verify(incidenciaRepository).save(any(IncidenciaEntity.class));
    }

    @Test
    void eliminarIncidenciaDebeSerLogico() {
        IncidenciaEntity incidencia = incidenciaActiva(1, null);

        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidencia));

        ResponseEntity<Object> response = incidenciaService.eliminarIncidencia(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(Boolean.FALSE, incidencia.getActivo());
        verify(incidenciaRepository).save(incidencia);
    }

    @Test
    void obtenerIncidenciaDebeResponderOk() {
        IncidenciaProjection projection = mock(IncidenciaProjection.class);
        IncidenciaResponse responseBody = new IncidenciaResponse();
        responseBody.setIdIncidencia(1);
        responseBody.setTitulo("No enciende el equipo");

        when(incidenciaRepository.obtenerIncidencia(1)).thenReturn(Optional.of(projection));
        when(genericMapper.toResponse(projection, IncidenciaResponse.class)).thenReturn(responseBody);

        ResponseEntity<Object> response = incidenciaService.obtenerIncidencia(1);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void clasificarIncidenciaDebeResponderOk() {
        ClasificarIncidenciaRequest request = new ClasificarIncidenciaRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(2);
        request.setIdSeveridad(1);
        request.setImpacto(2);
        request.setUrgencia(2);
        request.setReincidencia(0);

        IncidenciaEntity incidencia = incidenciaActiva(1, null);

        PrioridadEntity prioridad = new PrioridadEntity();
        prioridad.setIdPrioridad(3);

        SlaEntity sla = new SlaEntity();
        sla.setIdSla(1);
        sla.setMinutosResolucion(120);

        when(usuarioRepository.countUsuarioByRolId(2, 2)).thenReturn(1);
        when(severidadRepository.existsById(1)).thenReturn(true);
        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidencia));
        when(prioridadRepository.findByPuntaje(4)).thenReturn(Optional.of(prioridad));
        when(slaRepository.findActivoByIdSeveridad(1)).thenReturn(Optional.of(sla));
        when(estadoIncidenciaRepository.findById(2))
            .thenReturn(Optional.of(estado(2, "CLASIFICADA")));

        ResponseEntity<Object> response = incidenciaService.clasificarIncidencia(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(Integer.valueOf(4), incidencia.getPuntajePrioridad());
        verify(incidenciaRepository).save(incidencia);
    }

    @Test
    void asignarResponsableDebeResponderOk() {
        AsignarResponsableRequest request = new AsignarResponsableRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(2);
        request.setIdResponsable(3);

        when(usuarioRepository.countUsuarioByRolId(2, 2)).thenReturn(1);
        when(usuarioRepository.countUsuarioByRolId(3, 3)).thenReturn(1);
        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidenciaActiva(1, null)));
        when(estadoIncidenciaRepository.findById(3))
            .thenReturn(Optional.of(estado(3, "ASIGNADA")));

        ResponseEntity<Object> response = incidenciaService.asignarResponsable(request);

        assertEquals(200, response.getStatusCode().value());
        verify(incidenciaRepository).save(any(IncidenciaEntity.class));
    }

    @Test
    void iniciarAtencionDebeResponderOk() {
        ActualizarEstadoIncidenciaRequest request = new ActualizarEstadoIncidenciaRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(3);

        when(usuarioRepository.countUsuarioByRolId(3, 3)).thenReturn(1);
        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidenciaActiva(1, 3)));
        when(estadoIncidenciaRepository.findById(4))
            .thenReturn(Optional.of(estado(4, "EN_ATENCION")));

        ResponseEntity<Object> response = incidenciaService.iniciarAtencion(request);

        assertEquals(200, response.getStatusCode().value());
        verify(incidenciaRepository).save(any(IncidenciaEntity.class));
    }

    @Test
    void resolverIncidenciaDebeResponderOk() {
        ActualizarEstadoIncidenciaRequest request = new ActualizarEstadoIncidenciaRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(3);

        when(usuarioRepository.countUsuarioByRolId(3, 3)).thenReturn(1);
        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidenciaActiva(1, 3)));
        when(estadoIncidenciaRepository.findById(6))
            .thenReturn(Optional.of(estado(6, "RESUELTA")));

        ResponseEntity<Object> response = incidenciaService.resolverIncidencia(request);

        assertEquals(200, response.getStatusCode().value());
        verify(incidenciaRepository).save(any(IncidenciaEntity.class));
    }

    @Test
    void cerrarIncidenciaDebeResponderOk() {
        ActualizarEstadoIncidenciaRequest request = new ActualizarEstadoIncidenciaRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(4);

        when(usuarioRepository.countUsuarioByRolId(4, 5)).thenReturn(1);
        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidenciaActiva(1, null)));
        when(estadoIncidenciaRepository.findById(7))
            .thenReturn(Optional.of(estado(7, "CERRADA")));

        ResponseEntity<Object> response = incidenciaService.cerrarIncidencia(request);

        assertEquals(200, response.getStatusCode().value());
        verify(incidenciaRepository).save(any(IncidenciaEntity.class));
    }

    @Test
    void registrarEvidenciaDebeResponderOk() {
        RegistrarEvidenciaRequest request = new RegistrarEvidenciaRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(2);
        request.setNombreArchivo("captura.png");
        request.setRutaArchivo("/evidencias/captura.png");

        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidenciaActiva(1, null)));
        when(usuarioRepository.existsById(2)).thenReturn(true);

        ResponseEntity<Object> response = incidenciaService.registrarEvidencia(request);

        assertEquals(200, response.getStatusCode().value());
        verify(incidenciaEvidenciaRepository).save(any());
    }
}