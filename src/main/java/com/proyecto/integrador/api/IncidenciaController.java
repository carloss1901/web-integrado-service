package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.incidencia.ActualizarEstadoIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.ActualizarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.AsignarResponsableRequest;
import com.proyecto.integrador.model.request.incidencia.ClasificarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.CambiarEstadoIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarAccionCorrectivaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarComentarioRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarEvidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.ReabrirIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarRevisionSupervisorRequest;
import com.proyecto.integrador.model.response.IncidenciaAccionCorrectivaResponse;
import com.proyecto.integrador.model.response.IncidenciaComentarioResponse;
import com.proyecto.integrador.model.response.IncidenciaEvidenciaResponse;
import com.proyecto.integrador.model.response.IncidenciaHistorialResponse;
import com.proyecto.integrador.model.response.IncidenciaResponse;
import com.proyecto.integrador.model.response.IncidenciaBusquedaResponse;
import com.proyecto.integrador.service.IncidenciaService;
import com.proyecto.integrador.util.CustomPage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import org.springframework.web.bind.annotation.RequestAttribute;
import com.proyecto.integrador.model.response.IndicadoresIncidenciaResponse;
import com.proyecto.integrador.model.response.DashboardIncidenciasResponse;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

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

    @GetMapping(value = "listar-paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaResponse> listarIncidenciasPaginadas(
            @RequestParam(required = false) Integer idResponsable, @RequestParam(required = false) Integer idSeveridad,
            @RequestParam(required = false) Integer idEstado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(defaultValue = "1") @Min(1) Integer page, @RequestParam(defaultValue = "10") @Min(1) Integer size,
            Authentication authentication, @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado) {
        return incidenciaService.listarIncidenciasPaginadas(idReportanteParaBusqueda(authentication, idUsuarioAutenticado), idResponsable, idSeveridad, idEstado, inicio, fin, page, size);
    }

    @GetMapping(value = "mis-asignadas", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<IncidenciaResponse> listarMisIncidenciasAsignadas(
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        return incidenciaService.listarMisIncidenciasAsignadas(idUsuarioAutenticado);
    }

    @GetMapping(value = "mis-reportadas", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<IncidenciaResponse> listarMisIncidenciasReportadas(
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        return incidenciaService.listarMisIncidenciasReportadas(idUsuarioAutenticado);
    }

    @GetMapping(
            value = "buscar",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<IncidenciaBusquedaResponse> buscarIncidenciasPorTitulo(
            @NotBlank(message = "{message.required}")
            @RequestParam("texto") String texto,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado,
            Authentication authentication
    ) {
        return incidenciaService.buscarIncidenciasPorTitulo(
                texto,
                idReportanteParaBusqueda(authentication, idUsuarioAutenticado)
        );
    }

    @GetMapping(
            value = "buscar-por-fechas",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<IncidenciaBusquedaResponse> buscarIncidenciasPorFechas(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam("fin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado,
            Authentication authentication
    ) {
        return incidenciaService.buscarIncidenciasPorFechas(
                inicio,
                fin,
                idReportanteParaBusqueda(authentication, idUsuarioAutenticado)
        );
    }

    @GetMapping(value = "vencidas", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaResponse> listarIncidenciasVencidas(
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarIncidenciasVencidas(
                page,
                size
        );
    }

    @GetMapping(value = "proximas-a-vencer", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaResponse> listarIncidenciasProximasAVencer(
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "horas", defaultValue = "24") Integer horas,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarIncidenciasProximasAVencer(horas, page, size);
    }

    @GetMapping(
            value = "indicadores",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public IndicadoresIncidenciaResponse obtenerIndicadores() {
        return incidenciaService.obtenerIndicadores();
    }

    @GetMapping(value = "dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public DashboardIncidenciasResponse obtenerDashboardIncidencias(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) { return incidenciaService.obtenerDashboardIncidencias(inicio, fin); }

    @GetMapping(value = "{idIncidencia}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> obtenerIncidencia(
            @PathVariable("idIncidencia") Integer idIncidencia,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        return incidenciaService.obtenerIncidencia(
                idIncidencia,
                idUsuarioAutenticado
        );
    }

    @PostMapping(value = "registrar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarIncidencia(
            @Valid @RequestBody RegistrarIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        return incidenciaService.registrarIncidencia(request, idUsuarioAutenticado);
    }

    @PutMapping(value = "actualizar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> actualizarIncidencia(
            @Valid @RequestBody ActualizarIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.actualizarIncidencia(request);
    }

    @DeleteMapping(value = "eliminar/{idIncidencia}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> eliminarIncidencia(@PathVariable("idIncidencia") Integer idIncidencia) {
        return incidenciaService.eliminarIncidencia(idIncidencia);
    }

    @PostMapping(value = "clasificar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> clasificarIncidencia(
            @Valid @RequestBody ClasificarIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.clasificarIncidencia(request);
    }

    @PostMapping(value = "asignar-responsable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> asignarResponsable(
            @Valid @RequestBody AsignarResponsableRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.asignarResponsable(request);
    }

    @PostMapping(value = "iniciar-atencion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> iniciarAtencion(
            @Valid @RequestBody ActualizarEstadoIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.iniciarAtencion(request);
    }

    @PostMapping(value = "poner-en-espera", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> ponerEnEspera(
            @Valid @RequestBody CambiarEstadoIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.ponerEnEspera(request);
    }

    @PostMapping(value = "reanudar-atencion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reanudarAtencion(
            @Valid @RequestBody CambiarEstadoIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.reanudarAtencion(request);
    }

    @PostMapping(value = "resolver", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> resolverIncidencia(
            @Valid @RequestBody ActualizarEstadoIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.resolverIncidencia(request);
    }

    @PostMapping(value = "cerrar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> cerrarIncidencia(
            @Valid @RequestBody ActualizarEstadoIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.cerrarIncidencia(request);
    }

    @PostMapping(value = "revision-supervisor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarRevisionSupervisor(
            @Valid @RequestBody RegistrarRevisionSupervisorRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.registrarRevisionSupervisor(request);
    }

    @PostMapping(value = "reabrir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reabrirIncidencia(
            @Valid @RequestBody ReabrirIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.reabrirIncidencia(request);
    }

    @PostMapping(value = "cancelar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> cancelarIncidencia(
            @Valid @RequestBody CambiarEstadoIncidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.cancelarIncidencia(request);
    }

    @PostMapping(value = "evidencias", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarEvidencia(
            @Valid @RequestBody RegistrarEvidenciaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.registrarEvidencia(request);
    }

    @PostMapping(value = "acciones-correctivas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarAccionCorrectiva(
            @Valid @RequestBody RegistrarAccionCorrectivaRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.registrarAccionCorrectiva(request);
    }

    @PostMapping(value = "comentarios", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarComentario(
            @Valid @RequestBody RegistrarComentarioRequest request,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado
    ) {
        request.setIdUsuario(idUsuarioAutenticado);
        return incidenciaService.registrarComentario(request);
    }

    @GetMapping(value = "{idIncidencia}/historial", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaHistorialResponse> listarHistorial(
            @PathVariable("idIncidencia") Integer idIncidencia,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarHistorial(
                idIncidencia,
                idUsuarioAutenticado,
                page,
                size
        );
    }

    @GetMapping(value = "{idIncidencia}/evidencias", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaEvidenciaResponse> listarEvidencias(
            @PathVariable("idIncidencia") Integer idIncidencia,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarEvidencias(
                idIncidencia,
                idUsuarioAutenticado,
                page,
                size
        );
    }

    @GetMapping(value = "{idIncidencia}/acciones-correctivas", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaAccionCorrectivaResponse> listarAccionesCorrectivas(
            @PathVariable("idIncidencia") Integer idIncidencia,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarAccionesCorrectivas(
                idIncidencia,
                idUsuarioAutenticado,
                page,
                size
        );
    }

    @GetMapping(value = "{idIncidencia}/comentarios", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomPage<IncidenciaComentarioResponse> listarComentarios(
            @PathVariable("idIncidencia") Integer idIncidencia,
            @RequestAttribute("idUsuarioAutenticado") Integer idUsuarioAutenticado,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 1, message = "{message.minOne}")
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return incidenciaService.listarComentarios(
                idIncidencia,
                idUsuarioAutenticado,
                page,
                size
        );
    }

    private Integer idReportanteParaBusqueda(
            Authentication authentication,
            Integer idUsuarioAutenticado
    ) {
        boolean esReportante = authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_REPORTANTE".equals(authority.getAuthority()));

        return esReportante ? idUsuarioAutenticado : null;
    }
}
