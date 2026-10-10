package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.incidencia.ActualizarEstadoIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.ActualizarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.AsignarResponsableRequest;
import com.proyecto.integrador.model.request.incidencia.ClasificarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarAccionCorrectivaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarComentarioRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarEvidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarIncidenciaRequest;
import com.proyecto.integrador.model.response.IncidenciaAccionCorrectivaResponse;
import com.proyecto.integrador.model.response.IncidenciaComentarioResponse;
import com.proyecto.integrador.model.response.IncidenciaEvidenciaResponse;
import com.proyecto.integrador.model.response.IncidenciaHistorialResponse;
import com.proyecto.integrador.model.response.IncidenciaResponse;
import com.proyecto.integrador.util.CustomPage;
import org.springframework.http.ResponseEntity;
import com.proyecto.integrador.model.response.IncidenciaBusquedaResponse;
import java.util.List;
import java.time.LocalDate;

public interface IncidenciaService {
    List<IncidenciaResponse> listarIncidencias();
    List<IncidenciaBusquedaResponse> buscarIncidenciasPorTitulo(
            String texto
    );
    List<IncidenciaBusquedaResponse> buscarIncidenciasPorFechas(
            LocalDate inicio,
            LocalDate fin
    );
    CustomPage<IncidenciaEvidenciaResponse> listarEvidencias(
            Integer idIncidencia,
            Integer idUsuarioAutenticado,
            Integer page,
            Integer size
    );
    ResponseEntity<Object> obtenerIncidencia(
            Integer idIncidencia,
            Integer idUsuarioAutenticado
    );
    ResponseEntity<Object> registrarIncidencia(RegistrarIncidenciaRequest request);
    ResponseEntity<Object> actualizarIncidencia(ActualizarIncidenciaRequest request);
    ResponseEntity<Object> eliminarIncidencia(Integer idIncidencia);
    ResponseEntity<Object> clasificarIncidencia(ClasificarIncidenciaRequest request);
    ResponseEntity<Object> asignarResponsable(AsignarResponsableRequest request);
    ResponseEntity<Object> iniciarAtencion(ActualizarEstadoIncidenciaRequest request);
    ResponseEntity<Object> resolverIncidencia(ActualizarEstadoIncidenciaRequest request);
    ResponseEntity<Object> cerrarIncidencia(ActualizarEstadoIncidenciaRequest request);
    ResponseEntity<Object> registrarEvidencia(RegistrarEvidenciaRequest request);
    ResponseEntity<Object> registrarAccionCorrectiva(RegistrarAccionCorrectivaRequest request);
    ResponseEntity<Object> registrarComentario(RegistrarComentarioRequest request);
    CustomPage<IncidenciaHistorialResponse> listarHistorial(
            Integer idIncidencia,
            Integer idUsuarioAutenticado,
            Integer page,
            Integer size
    );
    CustomPage<IncidenciaResponse> listarIncidenciasVencidas(
            Integer page,
            Integer size
    );
    CustomPage<IncidenciaAccionCorrectivaResponse> listarAccionesCorrectivas(
            Integer idIncidencia,
            Integer idUsuarioAutenticado,
            Integer page,
            Integer size
    );
    CustomPage<IncidenciaComentarioResponse> listarComentarios(
            Integer idIncidencia,
            Integer idUsuarioAutenticado,
            Integer page,
            Integer size
    );
}
