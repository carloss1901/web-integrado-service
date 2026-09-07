package com.proyecto.integrador.api;

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
import com.proyecto.integrador.service.IncidenciaService;
import com.proyecto.integrador.util.CustomPage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Incidencia Controller")
@Validated
@RestController
@RequestMapping("/incidencias")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @GetMapping(value = "listar", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<IncidenciaResponse> listarIncidencias() {
        return incidenciaService.listarIncidencias();
    }

    @GetMapping(value = "vencidas", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaResponse> listarIncidenciasVencidas(
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarIncidenciasVencidas(page, size);
    }

    @GetMapping(value = "{idIncidencia}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> obtenerIncidencia(@PathVariable("idIncidencia") Integer idIncidencia) {
        return incidenciaService.obtenerIncidencia(idIncidencia);
    }

    @PostMapping(value = "registrar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarIncidencia(@Valid @RequestBody RegistrarIncidenciaRequest request) {
        return incidenciaService.registrarIncidencia(request);
    }

    @PutMapping(value = "actualizar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> actualizarIncidencia(@Valid @RequestBody ActualizarIncidenciaRequest request) {
        return incidenciaService.actualizarIncidencia(request);
    }

    @DeleteMapping(value = "eliminar/{idIncidencia}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> eliminarIncidencia(@PathVariable("idIncidencia") Integer idIncidencia) {
        return incidenciaService.eliminarIncidencia(idIncidencia);
    }

    @PostMapping(value = "clasificar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> clasificarIncidencia(@Valid @RequestBody ClasificarIncidenciaRequest request) {
        return incidenciaService.clasificarIncidencia(request);
    }

    @PostMapping(value = "asignar-responsable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> asignarResponsable(@Valid @RequestBody AsignarResponsableRequest request) {
        return incidenciaService.asignarResponsable(request);
    }

    @PostMapping(value = "iniciar-atencion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> iniciarAtencion(@Valid @RequestBody ActualizarEstadoIncidenciaRequest request) {
        return incidenciaService.iniciarAtencion(request);
    }

    @PostMapping(value = "resolver", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> resolverIncidencia(@Valid @RequestBody ActualizarEstadoIncidenciaRequest request) {
        return incidenciaService.resolverIncidencia(request);
    }

    @PostMapping(value = "cerrar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> cerrarIncidencia(@Valid @RequestBody ActualizarEstadoIncidenciaRequest request) {
        return incidenciaService.cerrarIncidencia(request);
    }

    @PostMapping(value = "evidencias", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarEvidencia(@Valid @RequestBody RegistrarEvidenciaRequest request) {
        return incidenciaService.registrarEvidencia(request);
    }

    @PostMapping(value = "acciones-correctivas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarAccionCorrectiva(
        @Valid @RequestBody RegistrarAccionCorrectivaRequest request) {
        return incidenciaService.registrarAccionCorrectiva(request);
    }

    @PostMapping(value = "comentarios", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarComentario(@Valid @RequestBody RegistrarComentarioRequest request) {
        return incidenciaService.registrarComentario(request);
    }

    @GetMapping(value = "{idIncidencia}/historial", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaHistorialResponse> listarHistorial(
        @PathVariable("idIncidencia") Integer idIncidencia,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarHistorial(idIncidencia, page, size);
    }

    @GetMapping(value = "{idIncidencia}/evidencias", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaEvidenciaResponse> listarEvidencias(
        @PathVariable("idIncidencia") Integer idIncidencia,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarEvidencias(idIncidencia, page, size);
    }

    @GetMapping(value = "{idIncidencia}/acciones-correctivas", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaAccionCorrectivaResponse> listarAccionesCorrectivas(
        @PathVariable("idIncidencia") Integer idIncidencia,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarAccionesCorrectivas(idIncidencia, page, size);
    }

    @GetMapping(value = "{idIncidencia}/comentarios", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaComentarioResponse> listarComentarios(
        @PathVariable("idIncidencia") Integer idIncidencia,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarComentarios(idIncidencia, page, size);
    }
}