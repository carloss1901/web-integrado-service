package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.incidencia.ActualizarEstadoIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.ActualizarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.AsignarResponsableRequest;
import com.proyecto.integrador.model.request.incidencia.ClasificarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarEvidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarIncidenciaRequest;
import com.proyecto.integrador.model.response.IncidenciaEvidenciaResponse;
import com.proyecto.integrador.model.response.IncidenciaHistorialResponse;
import com.proyecto.integrador.model.response.IncidenciaResponse;
import com.proyecto.integrador.service.IncidenciaService;
import com.proyecto.integrador.util.Constantes;
import com.proyecto.integrador.util.CustomPage;
import com.proyecto.integrador.util.ErrorGenerico;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Listar incidencias", description = "Listar incidencias", responses = {
        @ApiResponse(responseCode = Constantes.API_STATUS_200, description = Constantes.MSG_API_200,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = @ArraySchema(schema = @Schema(implementation = IncidenciaResponse.class)))),
        @ApiResponse(responseCode = Constantes.API_STATUS_400, description = Constantes.API_STATUS_400,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class))),
        @ApiResponse(responseCode = Constantes.API_STATUS_500, description = Constantes.MSF_API_500,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class)))
    })
    public List<IncidenciaResponse> listarIncidencias() {
        return incidenciaService.listarIncidencias();
    }

    @GetMapping(value = "{idIncidencia}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener incidencia", description = "Obtener incidencia")
    public ResponseEntity<Object> obtenerIncidencia(@PathVariable("idIncidencia") Integer idIncidencia) {
        return incidenciaService.obtenerIncidencia(idIncidencia);
    }

    @PostMapping(value = "registrar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registrar incidencia", description = "Registrar incidencia")
    public ResponseEntity<Object> registrarIncidencia(@Valid @RequestBody RegistrarIncidenciaRequest request) {
        return incidenciaService.registrarIncidencia(request);
    }

    @PutMapping(value = "actualizar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar incidencia", description = "Actualizar incidencia")
    public ResponseEntity<Object> actualizarIncidencia(@Valid @RequestBody ActualizarIncidenciaRequest request) {
        return incidenciaService.actualizarIncidencia(request);
    }

    @DeleteMapping(value = "eliminar/{idIncidencia}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Eliminar incidencia", description = "Eliminar incidencia")
    public ResponseEntity<Object> eliminarIncidencia(@PathVariable("idIncidencia") Integer idIncidencia) {
        return incidenciaService.eliminarIncidencia(idIncidencia);
    }

    @PostMapping(value = "clasificar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Clasificar incidencia", description = "Clasificar incidencia")
    public ResponseEntity<Object> clasificarIncidencia(@Valid @RequestBody ClasificarIncidenciaRequest request) {
        return incidenciaService.clasificarIncidencia(request);
    }

    @PostMapping(value = "asignar-responsable", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Asignar responsable", description = "Asignar responsable")
    public ResponseEntity<Object> asignarResponsable(@Valid @RequestBody AsignarResponsableRequest request) {
        return incidenciaService.asignarResponsable(request);
    }

    @PostMapping(value = "iniciar-atencion", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Iniciar atencion", description = "Iniciar atencion")
    public ResponseEntity<Object> iniciarAtencion(@Valid @RequestBody ActualizarEstadoIncidenciaRequest request) {
        return incidenciaService.iniciarAtencion(request);
    }

    @PostMapping(value = "resolver", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Resolver incidencia", description = "Resolver incidencia")
    public ResponseEntity<Object> resolverIncidencia(@Valid @RequestBody ActualizarEstadoIncidenciaRequest request) {
        return incidenciaService.resolverIncidencia(request);
    }

    @PostMapping(value = "cerrar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cerrar incidencia", description = "Cerrar incidencia")
    public ResponseEntity<Object> cerrarIncidencia(@Valid @RequestBody ActualizarEstadoIncidenciaRequest request) {
        return incidenciaService.cerrarIncidencia(request);
    }

    @PostMapping(value = "evidencias", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registrar evidencia", description = "Registrar evidencia")
    public ResponseEntity<Object> registrarEvidencia(@Valid @RequestBody RegistrarEvidenciaRequest request) {
        return incidenciaService.registrarEvidencia(request);
    }

    @GetMapping(value = "{idIncidencia}/historial", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar historial", description = "Listar historial de una incidencia")
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
    @Operation(summary = "Listar evidencias", description = "Listar evidencias de una incidencia")
    public CustomPage<IncidenciaEvidenciaResponse> listarEvidencias(
        @PathVariable("idIncidencia") Integer idIncidencia,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarEvidencias(idIncidencia, page, size);
    }
}
