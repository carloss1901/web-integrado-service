package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.incidencia.ActualizarEstadoIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.AsignarResponsableRequest;
import com.proyecto.integrador.model.request.incidencia.ClasificarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarEvidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarIncidenciaRequest;
import com.proyecto.integrador.model.response.IncidenciaResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IncidenciaService {
    List<IncidenciaResponse> listarIncidencias();
    ResponseEntity<Object> registrarIncidencia(RegistrarIncidenciaRequest request);
    ResponseEntity<Object> clasificarIncidencia(ClasificarIncidenciaRequest request);
    ResponseEntity<Object> asignarResponsable(AsignarResponsableRequest request);
    ResponseEntity<Object> iniciarAtencion(ActualizarEstadoIncidenciaRequest request);
    ResponseEntity<Object> resolverIncidencia(ActualizarEstadoIncidenciaRequest request);
    ResponseEntity<Object> cerrarIncidencia(ActualizarEstadoIncidenciaRequest request);
    ResponseEntity<Object> registrarEvidencia(RegistrarEvidenciaRequest request);
}
