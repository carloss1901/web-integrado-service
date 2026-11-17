package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.mapper.GenericMapper;
import com.proyecto.integrador.model.entity.EstadoIncidenciaEntity;
import com.proyecto.integrador.model.entity.CategoriaEntity;
import com.proyecto.integrador.model.entity.IncidenciaAccionCorrectivaEntity;
import com.proyecto.integrador.model.entity.IncidenciaComentarioEntity;
import com.proyecto.integrador.model.entity.IncidenciaEntity;
import com.proyecto.integrador.model.entity.IncidenciaEvidenciaEntity;
import com.proyecto.integrador.model.entity.IncidenciaHistorialEntity;
import com.proyecto.integrador.model.entity.IncidenciaRevisionSupervisorEntity;
import com.proyecto.integrador.model.entity.PrioridadEntity;
import com.proyecto.integrador.model.entity.SeveridadEntity;
import com.proyecto.integrador.model.entity.UbicacionEntity;
import com.proyecto.integrador.model.entity.SlaEntity;
import com.proyecto.integrador.model.enums.RolEnum;
import com.proyecto.integrador.model.enums.EstadoIncidenciaEnum;
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
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.repository.EstadoIncidenciaRepository;
import com.proyecto.integrador.repository.IncidenciaAccionCorrectivaRepository;
import com.proyecto.integrador.repository.IncidenciaComentarioRepository;
import com.proyecto.integrador.repository.IncidenciaEvidenciaRepository;
import com.proyecto.integrador.repository.IncidenciaHistorialRepository;
import com.proyecto.integrador.repository.IncidenciaRepository;
import com.proyecto.integrador.repository.IncidenciaRevisionSupervisorRepository;
import com.proyecto.integrador.repository.PrioridadRepository;
import com.proyecto.integrador.repository.SeveridadRepository;
import com.proyecto.integrador.repository.SlaRepository;
import com.proyecto.integrador.repository.UbicacionRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.service.IncidenciaService;
import com.proyecto.integrador.service.PrioridadCalculatorService;
import com.proyecto.integrador.util.CustomPage;
import com.proyecto.integrador.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.proyecto.integrador.model.response.IncidenciaBusquedaResponse;
import com.proyecto.integrador.model.response.IndicadoresIncidenciaResponse;
import com.proyecto.integrador.model.response.DashboardIncidenciasResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class IncidenciaServiceImpl implements IncidenciaService {

    private static final int ROL_REPORTANTE = RolEnum.REPORTANTE.getId();
    private static final int ROL_OPERADOR = RolEnum.OPERADOR_MESA_CONTROL.getId();
    private static final int ROL_RESPONSABLE = RolEnum.RESPONSABLE_ATENCION.getId();
    private static final int ROL_SUPERVISOR = RolEnum.SUPERVISOR.getId();
    private static final int ROL_ADMINISTRADOR = RolEnum.ADMINISTRADOR.getId();
    private static final int ESTADO_REGISTRADA = 1;
    private static final int ESTADO_CLASIFICADA = 2;
    private static final int ESTADO_ASIGNADA = 3;
    private static final int ESTADO_EN_ATENCION = 4;
    private static final int ESTADO_EN_ESPERA = 5;
    private static final int ESTADO_RESUELTA = 6;
    private static final int ESTADO_CERRADA = 7;
    private static final int ESTADO_CANCELADA = 8;
    private static final int ESTADO_REABIERTA = 9;

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    @Autowired
    private IncidenciaHistorialRepository incidenciaHistorialRepository;

    @Autowired
    private IncidenciaRevisionSupervisorRepository incidenciaRevisionSupervisorRepository;

    @Autowired
    private IncidenciaEvidenciaRepository incidenciaEvidenciaRepository;

    @Autowired
    private IncidenciaAccionCorrectivaRepository incidenciaAccionCorrectivaRepository;

    @Autowired
    private IncidenciaComentarioRepository incidenciaComentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private SeveridadRepository severidadRepository;

    @Autowired
    private PrioridadRepository prioridadRepository;

    @Autowired
    private SlaRepository slaRepository;

    @Autowired
    private EstadoIncidenciaRepository estadoIncidenciaRepository;

    @Autowired
    private GenericMapper genericMapper;

    @Autowired
    private PrioridadCalculatorService prioridadCalculatorService;

    @Value("${app.incidencia.prioridad.ventana-reincidencia-dias:30}")
    private long ventanaReincidenciaDias;

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaResponse> listarIncidencias() {
        return completarEstadoRevision(genericMapper.toResponseList(incidenciaRepository.listarIncidencias(), IncidenciaResponse.class));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<IncidenciaResponse> listarIncidenciasPaginadas(Integer idReportante, Integer idResponsable, Integer idSeveridad, Integer idEstado, LocalDate inicio, LocalDate fin, Integer page, Integer size) {
        if (inicio != null && fin != null && inicio.isAfter(fin)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha inicial no puede ser posterior a la final");
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<IncidenciaEntity> resultado = incidenciaRepository.listarPaginado(idReportante, idResponsable, idSeveridad, idEstado,
            inicio == null ? null : inicio.atStartOfDay(), fin == null ? null : fin.plusDays(1).atStartOfDay().minusNanos(1), pageable);
        List<IncidenciaResponse> contenido = resultado.getContent().stream().map(this::mapearIncidenciaPaginada).toList();
        return new CustomPage<>(new PageImpl<>(contenido, pageable, resultado.getTotalElements()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaResponse> listarMisIncidenciasAsignadas(Integer idResponsable) {
        return completarEstadoRevision(genericMapper.toResponseList(
            incidenciaRepository.listarIncidenciasPorResponsable(idResponsable),
            IncidenciaResponse.class
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaResponse> listarMisIncidenciasReportadas(Integer idReportante) {
        return completarEstadoRevision(genericMapper.toResponseList(
            incidenciaRepository.listarIncidenciasPorReportante(idReportante),
            IncidenciaResponse.class
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public IndicadoresIncidenciaResponse obtenerIndicadores() {
        return incidenciaRepository.obtenerIndicadores();
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardIncidenciasResponse obtenerDashboardIncidencias(LocalDate inicio, LocalDate fin) {
        LocalDate fechaInicio = inicio == null ? LocalDate.now().minusDays(30) : inicio;
        LocalDate fechaFin = fin == null ? LocalDate.now() : fin;
        if (fechaInicio.isAfter(fechaFin)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha inicial no puede ser posterior a la final");
        LocalDateTime desde = fechaInicio.atStartOfDay();
        LocalDateTime hasta = fechaFin.plusDays(1).atStartOfDay().minusNanos(1);
        Long total = incidenciaRepository.totalPeriodo(desde, hasta);
        Long dentro = incidenciaRepository.cumplimientoSla(desde, hasta);
        return new DashboardIncidenciasResponse(total, dentro, Math.max(0, total - dentro),
            incidenciaRepository.contarPorZona(desde, hasta), incidenciaRepository.contarPorCategoria(desde, hasta),
            incidenciaRepository.contarPorSeveridad(desde, hasta), incidenciaRepository.cargaActivaResponsables());
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaBusquedaResponse> buscarIncidenciasPorTitulo(
            String texto,
            Integer idReportante
    ) {
        String textoBusqueda = texto == null
                ? ""
                : texto.trim();

        return incidenciaRepository.buscarResumenPorTitulo(
                textoBusqueda,
                idReportante
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaBusquedaResponse> buscarIncidenciasPorFechas(
            LocalDate inicio,
            LocalDate fin,
            Integer idReportante
    ) {
        if (inicio.isAfter(fin)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha inicial no puede ser posterior a la fecha final"
            );
        }

        LocalDateTime fechaInicio = inicio.atStartOfDay();

        LocalDateTime fechaFin = fin
                .plusDays(1)
                .atStartOfDay()
                .minusNanos(1);

        return incidenciaRepository.buscarPorRangoFechas(
                fechaInicio,
                fechaFin,
                idReportante
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<IncidenciaResponse> listarIncidenciasVencidas(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return mapearPagina(incidenciaRepository.listarIncidenciasVencidas(pageable),
            pageable, IncidenciaResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<IncidenciaResponse> listarIncidenciasProximasAVencer(Integer horas, Integer page, Integer size) {
        int horasConsulta = horas == null ? 24 : Math.min(Math.max(horas, 1), 168);
        Pageable pageable = PageRequest.of(page - 1, size);
        LocalDateTime ahora = LocalDateTime.now();
        return mapearPagina(incidenciaRepository.listarIncidenciasProximasAVencer(
            ahora, ahora.plusHours(horasConsulta), pageable), pageable, IncidenciaResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerIncidencia(
            Integer idIncidencia,
            Integer idUsuarioAutenticado
    ) {
        IncidenciaEntity incidencia = obtenerIncidenciaActiva(idIncidencia);

        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }

        if (!puedeConsultarIncidencia(idUsuarioAutenticado, incidencia)) {
            return errorForbidden("No tiene permiso para consultar esta incidencia");
        }

        return incidenciaRepository.obtenerIncidencia(idIncidencia)
                .map(projection -> MessageResponse.setResponse(
                        Boolean.TRUE,
                        HttpStatus.OK,
                        "Proceso exitoso",
                        completarEstadoRevision(genericMapper.toResponse(
                                projection,
                                IncidenciaResponse.class
                        ))
                ))
                .orElseGet(() -> errorNotFound("La incidencia no existe"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarIncidencia(RegistrarIncidenciaRequest request, Integer idReportante) {
        if (!tieneRol(idReportante, ROL_REPORTANTE)
                && !tieneRol(idReportante, ROL_ADMINISTRADOR)) {
            return error("El usuario no tiene rol REPORTANTE");
        }
        if (!categoriaRepository.existsById(request.getIdCategoria())) {
            return error("La categoria no existe");
        }
        if (!ubicacionRepository.existsById(request.getIdUbicacion())) {
            return error("La ubicacion no existe");
        }

        LocalDateTime ahora = LocalDateTime.now();
        IncidenciaEntity incidencia = new IncidenciaEntity();
        incidencia.setCodigo(generarCodigo(ahora));
        incidencia.setIdReportante(idReportante);
        incidencia.setIdCategoria(request.getIdCategoria());
        incidencia.setIdUbicacion(request.getIdUbicacion());
        incidencia.setIdEstado(obtenerEstado(ESTADO_REGISTRADA).getIdEstado());
        incidencia.setTitulo(request.getTitulo());
        incidencia.setDescripcion(request.getDescripcion());
        incidencia.setFechaRegistro(ahora);
        incidencia.setActivo(Boolean.TRUE);

        incidenciaRepository.save(incidencia);
        registrarHistorial(incidencia.getIdIncidencia(), idReportante, "REGISTRO_INCIDENCIA",
            null, incidencia.getCodigo(), "Registro inicial de incidencia");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia registrada correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> actualizarIncidencia(ActualizarIncidenciaRequest request) {
        if (!categoriaRepository.existsById(request.getIdCategoria())) {
            return error("La categoria no existe");
        }
        if (!ubicacionRepository.existsById(request.getIdUbicacion())) {
            return error("La ubicacion no existe");
        }

        IncidenciaEntity incidencia = obtenerIncidenciaActiva(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        incidencia.setIdCategoria(request.getIdCategoria());
        incidencia.setIdUbicacion(request.getIdUbicacion());
        incidencia.setTitulo(request.getTitulo());
        incidencia.setDescripcion(request.getDescripcion());
        incidencia.setFecMod(LocalDateTime.now());
        if (incidencia.getImpacto() != null && incidencia.getUrgencia() != null) {
            recalcularPrioridad(incidencia, request.getIdUsuario(), LocalDateTime.now());
        }
        incidenciaRepository.save(incidencia);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia actualizada correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> eliminarIncidencia(Integer idIncidencia) {
        IncidenciaEntity incidencia = obtenerIncidenciaActiva(idIncidencia);
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }

        incidencia.setActivo(Boolean.FALSE);
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia eliminada correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> clasificarIncidencia(ClasificarIncidenciaRequest request) {
        if (!tieneRol(request.getIdUsuario(), ROL_OPERADOR)) {
            return error("El usuario no tiene rol OPERADOR_MESA_CONTROL");
        }
        if (!severidadRepository.existsById(request.getIdSeveridad())) {
            return error("La severidad no existe");
        }
        if (!criterioValido(request.getImpacto(), 1, 4)
            || !criterioValido(request.getUrgencia(), 1, 4)) {
            return error("Impacto y urgencia deben tener valores entre 1 y 4");
        }

        IncidenciaEntity incidencia = obtenerIncidenciaActiva(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        ResponseEntity<Object> transicionInvalida = validarTransicion(incidencia, ESTADO_CLASIFICADA);
        if (transicionInvalida != null) {
            return transicionInvalida;
        }

        LocalDateTime ahora = LocalDateTime.now();
        int reincidencia = calcularReincidencia(incidencia, ahora);
        PrioridadCalculatorService.Resultado calculo = prioridadCalculatorService.calcular(
            request.getImpacto(), request.getUrgencia(), reincidencia);
        PrioridadEntity prioridad = prioridadRepository.findByPuntaje(calculo.puntuacion())
            .orElseThrow(() -> new IllegalStateException("No existe prioridad configurada para el puntaje " + calculo.puntuacion()));
        SlaEntity sla = slaRepository.findActivoByIdSeveridad(request.getIdSeveridad())
            .orElseThrow(() -> new IllegalStateException("No existe SLA configurado para la severidad"));

        registrarCambioSiAplica(incidencia, request.getIdUsuario(), "CAMBIO_SEVERIDAD",
            incidencia.getIdSeveridad(), request.getIdSeveridad(), "Clasificacion de severidad");
        registrarAuditoriaPrioridad(incidencia, request.getIdUsuario(), prioridad, calculo,
            request.getImpacto(), request.getUrgencia(), reincidencia);

        incidencia.setIdSeveridad(request.getIdSeveridad());
        incidencia.setImpacto(request.getImpacto());
        incidencia.setUrgencia(request.getUrgencia());
        incidencia.setReincidencia(reincidencia);
        incidencia.setPuntajePrioridad(calculo.puntuacion());
        incidencia.setIdPrioridad(prioridad.getIdPrioridad());
        incidencia.setIdSla(sla.getIdSla());
        incidencia.setFechaLimite(incidencia.getFechaRegistro().plusMinutes(sla.getMinutosResolucion()));
        incidencia.setIdEstado(obtenerEstado(ESTADO_CLASIFICADA).getIdEstado());
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);

        registrarHistorial(incidencia.getIdIncidencia(), request.getIdUsuario(), "CLASIFICACION",
            null, calculo.puntuacion().toPlainString(), "Incidencia clasificada y priorizada: "
                + explicacion(calculo, request.getImpacto(), request.getUrgencia(), reincidencia));

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia clasificada correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> asignarResponsable(AsignarResponsableRequest request) {
        if (!tieneRol(request.getIdUsuario(), ROL_OPERADOR)) {
            return error("El usuario no tiene rol OPERADOR_MESA_CONTROL");
        }
        if (!tieneRol(request.getIdResponsable(), ROL_RESPONSABLE)) {
            return error("El usuario asignado no tiene rol RESPONSABLE_ATENCION");
        }

        IncidenciaEntity incidencia = obtenerIncidenciaActiva(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        boolean esPrimeraAsignacion = Integer.valueOf(ESTADO_CLASIFICADA).equals(incidencia.getIdEstado());
        boolean esReasignacion = Integer.valueOf(ESTADO_ASIGNADA).equals(incidencia.getIdEstado());
        if (!esPrimeraAsignacion && !esReasignacion) {
            return error("Solo se puede asignar o reasignar una incidencia clasificada o asignada");
        }
        if (esPrimeraAsignacion) {
            ResponseEntity<Object> transicionInvalida = validarTransicion(incidencia, ESTADO_ASIGNADA);
            if (transicionInvalida != null) {
                return transicionInvalida;
            }
        }

        Integer responsableAnterior = incidencia.getIdResponsable();
        incidencia.setIdResponsable(request.getIdResponsable());
        incidencia.setIdEstado(obtenerEstado(ESTADO_ASIGNADA).getIdEstado());
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);

        registrarHistorial(incidencia.getIdIncidencia(), request.getIdUsuario(), "ASIGNACION_RESPONSABLE",
            valor(responsableAnterior), request.getIdResponsable().toString(), "Asignacion de responsable de atencion");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Responsable asignado correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> iniciarAtencion(ActualizarEstadoIncidenciaRequest request) {
        return cambiarEstadoPorResponsable(request.getIdIncidencia(), request.getIdUsuario(),
            ESTADO_EN_ATENCION, "INICIO_ATENCION", "Inicio de atencion de incidencia", false,
            null, ESTADO_ASIGNADA);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> resolverIncidencia(ActualizarEstadoIncidenciaRequest request) {
        if (request.getDescripcionResolucion() == null || request.getDescripcionResolucion().isBlank()) {
            return error("La descripcion de resolucion es obligatoria");
        }
        return cambiarEstadoPorResponsable(request.getIdIncidencia(), request.getIdUsuario(),
            ESTADO_RESUELTA, "RESOLUCION", "Resolucion de incidencia", true,
            request.getDescripcionResolucion().trim(), ESTADO_EN_ATENCION);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> ponerEnEspera(CambiarEstadoIncidenciaRequest request) {
        return cambiarEstadoPorResponsable(request.getIdIncidencia(), request.getIdUsuario(),
            ESTADO_EN_ESPERA, "EN_ESPERA", "Incidencia puesta en espera", false,
            request.getObservacion().trim(), ESTADO_EN_ATENCION);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> reanudarAtencion(CambiarEstadoIncidenciaRequest request) {
        return cambiarEstadoPorResponsable(request.getIdIncidencia(), request.getIdUsuario(),
            ESTADO_EN_ATENCION, "REANUDACION_ATENCION", "Atencion reanudada", false,
            request.getObservacion().trim(), ESTADO_EN_ESPERA, ESTADO_REABIERTA);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cerrarIncidencia(
            ActualizarEstadoIncidenciaRequest request
    ) {
        if (!tieneRol(request.getIdUsuario(), ROL_SUPERVISOR)) {
            return errorForbidden(
                    "Solo un supervisor puede cerrar incidencias"
            );
        }

        IncidenciaEntity incidencia =
                obtenerIncidenciaActiva(request.getIdIncidencia());

        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        ResponseEntity<Object> transicionInvalida = validarTransicion(incidencia, ESTADO_CERRADA);
        if (transicionInvalida != null) {
            return transicionInvalida;
        }
        if (esCritica(incidencia) && !tieneRevisionSupervisorAprobada(incidencia.getIdIncidencia())) {
            return error("La incidencia critica requiere una revision aprobada por supervisor antes del cierre");
        }

        String estadoAnterior = valor(incidencia.getIdEstado());

        incidencia.setIdEstado(
                obtenerEstado(ESTADO_CERRADA).getIdEstado()
        );
        incidencia.setFechaCierre(LocalDateTime.now());
        incidencia.setFecMod(LocalDateTime.now());

        incidenciaRepository.save(incidencia);

        registrarHistorial(
                incidencia.getIdIncidencia(),
                request.getIdUsuario(),
                "CIERRE",
                estadoAnterior,
                incidencia.getIdEstado().toString(),
                "Cierre definitivo de incidencia"
        );

        return MessageResponse.setResponse(
                Boolean.TRUE,
                HttpStatus.OK,
                "Incidencia cerrada correctamente"
        );
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarRevisionSupervisor(RegistrarRevisionSupervisorRequest request) {
        if (!tieneRol(request.getIdUsuario(), ROL_SUPERVISOR)) {
            return errorForbidden("Solo un supervisor puede registrar la revision");
        }
        IncidenciaEntity incidencia = obtenerIncidenciaActiva(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        if (!esCritica(incidencia)) {
            return error("La revision de supervisor solo aplica a incidencias criticas");
        }
        if (!Integer.valueOf(ESTADO_RESUELTA).equals(incidencia.getIdEstado())) {
            return error("La incidencia critica debe estar resuelta antes de su revision");
        }

        IncidenciaRevisionSupervisorEntity revision = new IncidenciaRevisionSupervisorEntity();
        revision.setIdIncidencia(incidencia.getIdIncidencia());
        revision.setIdSupervisor(request.getIdUsuario());
        revision.setAprobada(request.getAprobada());
        revision.setObservacion(request.getObservacion().trim());
        revision.setFechaRevision(LocalDateTime.now());
        revision.setActivo(Boolean.TRUE);
        incidenciaRevisionSupervisorRepository.save(revision);

        registrarHistorial(incidencia.getIdIncidencia(), request.getIdUsuario(), "REVISION_SUPERVISOR",
            null, request.getAprobada().toString(), "Revision " + (request.getAprobada() ? "aprobada" : "rechazada")
                + " por supervisor: " + request.getObservacion().trim());

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK,
            request.getAprobada() ? "Revision aprobada correctamente" : "Revision rechazada correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> reabrirIncidencia(ReabrirIncidenciaRequest request) {
        if (!tieneRol(request.getIdUsuario(), ROL_SUPERVISOR)) {
            return errorForbidden("Solo un supervisor puede reabrir incidencias");
        }

        IncidenciaEntity incidencia = obtenerIncidenciaActiva(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        ResponseEntity<Object> transicionInvalida = validarTransicion(incidencia, ESTADO_REABIERTA);
        if (transicionInvalida != null) {
            return transicionInvalida;
        }

        String estadoAnterior = valor(incidencia.getIdEstado());
        incidencia.setIdEstado(obtenerEstado(ESTADO_REABIERTA).getIdEstado());
        incidencia.setFechaResolucion(null);
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);

        registrarHistorial(incidencia.getIdIncidencia(), request.getIdUsuario(), "REAPERTURA",
            estadoAnterior, incidencia.getIdEstado().toString(), request.getObservacion().trim());

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia reabierta correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cancelarIncidencia(CambiarEstadoIncidenciaRequest request) {
        if (!tieneRol(request.getIdUsuario(), ROL_OPERADOR)) {
            return errorForbidden("Solo un operador puede cancelar incidencias");
        }

        IncidenciaEntity incidencia = obtenerIncidenciaActiva(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        ResponseEntity<Object> transicionInvalida = validarTransicion(incidencia, ESTADO_CANCELADA);
        if (transicionInvalida != null) {
            return transicionInvalida;
        }

        String estadoAnterior = valor(incidencia.getIdEstado());
        incidencia.setIdEstado(obtenerEstado(ESTADO_CANCELADA).getIdEstado());
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);
        registrarHistorial(incidencia.getIdIncidencia(), request.getIdUsuario(), "CANCELACION",
            estadoAnterior, incidencia.getIdEstado().toString(), request.getObservacion().trim());

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia cancelada correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarEvidencia(RegistrarEvidenciaRequest request) {
        IncidenciaEntity incidencia = obtenerIncidenciaActiva(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        if (!usuarioRepository.existsById(request.getIdUsuario())) {
            return error("El usuario no existe");
        }
        if (!puedeConsultarIncidencia(request.getIdUsuario(), incidencia)) {
            return errorForbidden(
                    "No tiene permiso para registrar evidencias en esta incidencia"
            );
        }

        IncidenciaEvidenciaEntity evidencia = new IncidenciaEvidenciaEntity();
        evidencia.setIdIncidencia(request.getIdIncidencia());
        evidencia.setIdUsuario(request.getIdUsuario());
        evidencia.setNombreArchivo(request.getNombreArchivo());
        evidencia.setRutaArchivo(request.getRutaArchivo());
        evidencia.setDescripcion(request.getDescripcion());
        evidencia.setFechaRegistro(LocalDateTime.now());
        evidencia.setActivo(Boolean.TRUE);
        incidenciaEvidenciaRepository.save(evidencia);

        registrarHistorial(request.getIdIncidencia(), request.getIdUsuario(), "REGISTRO_EVIDENCIA",
            null, request.getNombreArchivo(), "Registro de evidencia de incidencia");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Evidencia registrada correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarAccionCorrectiva(RegistrarAccionCorrectivaRequest request) {
        IncidenciaEntity incidencia = obtenerIncidenciaActiva(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        if (!puedeConsultarIncidencia(request.getIdUsuario(), incidencia)) {
            return errorForbidden(
                    "No tiene permiso para registrar acciones correctivas en esta incidencia"
            );
        }
        if (!usuarioRepository.existsById(request.getIdUsuario())) {
            return error("El usuario no existe");
        }

        IncidenciaAccionCorrectivaEntity accion = new IncidenciaAccionCorrectivaEntity();
        accion.setIdIncidencia(request.getIdIncidencia());
        accion.setIdUsuario(request.getIdUsuario());
        accion.setDescripcion(request.getDescripcion());
        accion.setFechaRegistro(LocalDateTime.now());
        accion.setActivo(Boolean.TRUE);
        incidenciaAccionCorrectivaRepository.save(accion);

        registrarHistorial(request.getIdIncidencia(), request.getIdUsuario(), "REGISTRO_ACCION_CORRECTIVA",
            null, accion.getDescripcion(), "Registro de accion correctiva");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Accion correctiva registrada correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarComentario(RegistrarComentarioRequest request) {
        IncidenciaEntity incidencia = obtenerIncidenciaActiva(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        if (!puedeConsultarIncidencia(request.getIdUsuario(), incidencia)) {
            return errorForbidden(
                    "No tiene permiso para comentar esta incidencia"
            );
        }
        if (!usuarioRepository.existsById(request.getIdUsuario())) {
            return error("El usuario no existe");
        }

        IncidenciaComentarioEntity comentario = new IncidenciaComentarioEntity();
        comentario.setIdIncidencia(request.getIdIncidencia());
        comentario.setIdUsuario(request.getIdUsuario());
        comentario.setComentario(request.getComentario());
        comentario.setFechaRegistro(LocalDateTime.now());
        comentario.setActivo(Boolean.TRUE);
        incidenciaComentarioRepository.save(comentario);

        registrarHistorial(request.getIdIncidencia(), request.getIdUsuario(), "REGISTRO_COMENTARIO",
            null, request.getComentario(), "Registro de comentario");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Comentario registrado correctamente");
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<IncidenciaHistorialResponse> listarHistorial(
            Integer idIncidencia,
            Integer idUsuarioAutenticado,
            Integer page,
            Integer size
    ) {
        validarAccesoConsulta(
                idIncidencia,
                idUsuarioAutenticado
        );

        Pageable pageable = PageRequest.of(page - 1, size);

        return mapearPagina(
                incidenciaHistorialRepository.listarHistorial(
                        idIncidencia,
                        pageable
                ),
                pageable,
                IncidenciaHistorialResponse.class
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<IncidenciaEvidenciaResponse> listarEvidencias(
            Integer idIncidencia,
            Integer idUsuarioAutenticado,
            Integer page,
            Integer size
    ) {
        validarAccesoConsulta(
                idIncidencia,
                idUsuarioAutenticado
        );

        Pageable pageable = PageRequest.of(page - 1, size);

        return mapearPagina(
                incidenciaEvidenciaRepository.listarEvidencias(
                        idIncidencia,
                        pageable
                ),
                pageable,
                IncidenciaEvidenciaResponse.class
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<IncidenciaAccionCorrectivaResponse> listarAccionesCorrectivas(
            Integer idIncidencia,
            Integer idUsuarioAutenticado,
            Integer page,
            Integer size
    ) {
        validarAccesoConsulta(
                idIncidencia,
                idUsuarioAutenticado
        );

        Pageable pageable = PageRequest.of(page - 1, size);

        return mapearPagina(
                incidenciaAccionCorrectivaRepository
                        .listarAccionesCorrectivas(
                                idIncidencia,
                                pageable
                        ),
                pageable,
                IncidenciaAccionCorrectivaResponse.class
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<IncidenciaComentarioResponse> listarComentarios(
            Integer idIncidencia,
            Integer idUsuarioAutenticado,
            Integer page,
            Integer size
    ) {
        validarAccesoConsulta(
                idIncidencia,
                idUsuarioAutenticado
        );

        Pageable pageable = PageRequest.of(page - 1, size);

        return mapearPagina(
                incidenciaComentarioRepository.listarComentarios(
                        idIncidencia,
                        pageable
                ),
                pageable,
                IncidenciaComentarioResponse.class
        );
    }

    private <P, T> CustomPage<T> mapearPagina(Page<P> result, Pageable pageable, Class<T> targetClass) {
        List<T> content = genericMapper.toResponseList(result.getContent(), targetClass);
        return new CustomPage<>(new PageImpl<>(content, pageable, result.getTotalElements()));
    }

    private ResponseEntity<Object> cambiarEstadoPorResponsable(
        Integer idIncidencia,
        Integer idUsuario,
        int estadoNuevo,
        String tipoEvento,
        String descripcion,
        boolean registrarResolucion,
        String observacion,
        int... estadosOrigenPermitidos
    ) {
        if (!tieneRol(idUsuario, ROL_RESPONSABLE)) {
            return error("El usuario no tiene rol RESPONSABLE_ATENCION");
        }

        IncidenciaEntity incidencia = obtenerIncidenciaActiva(idIncidencia);
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        if (!idUsuario.equals(incidencia.getIdResponsable())) {
            return error("La incidencia no esta asignada al usuario indicado");
        }
        boolean estadoOrigenPermitido = false;
        for (int estadoOrigen : estadosOrigenPermitidos) {
            if (Integer.valueOf(estadoOrigen).equals(incidencia.getIdEstado())) {
                estadoOrigenPermitido = true;
                break;
            }
        }
        if (!estadoOrigenPermitido) {
            return error("La accion no esta permitida para el estado actual de la incidencia");
        }
        ResponseEntity<Object> transicionInvalida = validarTransicion(incidencia, estadoNuevo);
        if (transicionInvalida != null) {
            return transicionInvalida;
        }

        String estadoAnterior = valor(incidencia.getIdEstado());
        incidencia.setIdEstado(obtenerEstado(estadoNuevo).getIdEstado());
        if (registrarResolucion) {
            incidencia.setFechaResolucion(LocalDateTime.now());
        }
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);

        if (observacion != null) {
            IncidenciaAccionCorrectivaEntity accion = new IncidenciaAccionCorrectivaEntity();
            accion.setIdIncidencia(incidencia.getIdIncidencia());
            accion.setIdUsuario(idUsuario);
            accion.setDescripcion(observacion);
            accion.setFechaRegistro(LocalDateTime.now());
            accion.setActivo(Boolean.TRUE);
            incidenciaAccionCorrectivaRepository.save(accion);
        }

        registrarHistorial(incidencia.getIdIncidencia(), idUsuario, tipoEvento,
            estadoAnterior, incidencia.getIdEstado().toString(), observacion == null
                ? descripcion
                : descripcion + ": " + observacion);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, descripcion);
    }

    private ResponseEntity<Object> validarTransicion(IncidenciaEntity incidencia, int estadoDestinoId) {
        EstadoIncidenciaEnum estadoActual = EstadoIncidenciaEnum.fromId(incidencia.getIdEstado());
        EstadoIncidenciaEnum estadoDestino = EstadoIncidenciaEnum.fromId(estadoDestinoId);
        if (estadoActual == null || estadoDestino == null || !estadoActual.permiteTransicionA(estadoDestino)) {
            return error("La transicion de estado solicitada no esta permitida");
        }
        return null;
    }

    private void validarAccesoConsulta(
            Integer idIncidencia,
            Integer idUsuarioAutenticado
    ) {
        IncidenciaEntity incidencia =
                obtenerIncidenciaActiva(idIncidencia);

        if (incidencia == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "La incidencia no existe"
            );
        }

        if (!puedeConsultarIncidencia(
                idUsuarioAutenticado,
                incidencia
        )) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tiene permiso para consultar esta incidencia"
            );
        }
    }

    private boolean puedeConsultarIncidencia(
            Integer idUsuario,
            IncidenciaEntity incidencia
    ) {
        if (incidencia == null || idUsuario == null) {
            return false;
        }

        boolean accesoGeneral =
                tieneRol(idUsuario, ROL_ADMINISTRADOR)
                        || tieneRol(idUsuario, ROL_OPERADOR)
                        || tieneRol(idUsuario, ROL_SUPERVISOR);

        if (accesoGeneral) {
            return true;
        }

        boolean esReportantePropietario =
                tieneRol(idUsuario, ROL_REPORTANTE)
                        && idUsuario.equals(incidencia.getIdReportante());

        boolean esResponsableAsignado =
                tieneRol(idUsuario, ROL_RESPONSABLE)
                        && idUsuario.equals(incidencia.getIdResponsable());

        return esReportantePropietario || esResponsableAsignado;
    }

    private boolean tieneRol(Integer idUsuario, int idRol) {
        return idUsuario != null && usuarioRepository.countUsuarioByRolId(idUsuario, idRol) > 0;
    }

    private boolean esCritica(IncidenciaEntity incidencia) {
        if (incidencia.getIdSeveridad() == null) {
            return false;
        }
        return severidadRepository.findById(incidencia.getIdSeveridad())
            .map(severidad -> severidad.getNivel() != null && severidad.getNivel() >= 4)
            .orElse(false);
    }

    private boolean tieneRevisionSupervisorAprobada(Integer idIncidencia) {
        return incidenciaRevisionSupervisorRepository
            .findTopByIdIncidenciaAndActivoTrueOrderByFechaRevisionDesc(idIncidencia)
            .map(revision -> Boolean.TRUE.equals(revision.getAprobada()))
            .orElse(false);
    }

    private IncidenciaResponse completarEstadoRevision(IncidenciaResponse incidencia) {
        incidencia.setRevisionSupervisorAprobada(tieneRevisionSupervisorAprobada(incidencia.getIdIncidencia()));
        return incidencia;
    }

    private List<IncidenciaResponse> completarEstadoRevision(List<IncidenciaResponse> incidencias) {
        incidencias.forEach(this::completarEstadoRevision);
        return incidencias;
    }

    private IncidenciaEntity obtenerIncidenciaActiva(Integer idIncidencia) {
        if (idIncidencia == null) {
            return null;
        }
        return incidenciaRepository.findById(idIncidencia)
            .filter(incidencia -> Boolean.TRUE.equals(incidencia.getActivo()))
            .orElse(null);
    }

    private IncidenciaResponse mapearIncidenciaPaginada(IncidenciaEntity item) {
        IncidenciaResponse response = new IncidenciaResponse();
        response.setIdIncidencia(item.getIdIncidencia()); response.setCodigo(item.getCodigo()); response.setTitulo(item.getTitulo());
        response.setReportante(nombreUsuario(item.getIdReportante()));
        response.setResponsable(nombreUsuario(item.getIdResponsable()));
        response.setCategoria(nombreCategoria(item.getIdCategoria()));
        response.setUbicacion(nombreUbicacion(item.getIdUbicacion()));
        response.setSeveridad(nombreSeveridad(item.getIdSeveridad()));
        response.setPrioridad(nombrePrioridad(item.getIdPrioridad()));
        response.setDescripcion(item.getDescripcion());
        response.setFechaRegistro(item.getFechaRegistro() == null ? null : item.getFechaRegistro().toString());
        response.setFechaLimite(item.getFechaLimite() == null ? null : item.getFechaLimite().toString());
        response.setFechaResolucion(item.getFechaResolucion() == null ? null : item.getFechaResolucion().toString());
        response.setFechaCierre(item.getFechaCierre() == null ? null : item.getFechaCierre().toString());
        response.setImpacto(item.getImpacto()); response.setUrgencia(item.getUrgencia()); response.setReincidencia(item.getReincidencia());
        response.setPuntuacionPrioridad(item.getPuntajePrioridad()); response.setEstado(obtenerEstado(item.getIdEstado()).getNombre());
        response.setRevisionSupervisorAprobada(tieneRevisionSupervisorAprobada(item.getIdIncidencia()));
        return response;
    }

    private String nombreUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            return null;
        }
        return usuarioRepository.findById(idUsuario)
            .map(usuario -> String.join(" ", java.util.stream.Stream.of(
                    usuario.getNombres(), usuario.getApePaterno(), usuario.getApeMaterno())
                .filter(java.util.Objects::nonNull)
                .filter(nombre -> !nombre.isBlank())
                .toList()))
            .orElse(null);
    }

    private String nombreCategoria(Integer idCategoria) {
        return idCategoria == null ? null : categoriaRepository.findById(idCategoria).map(CategoriaEntity::getNombre).orElse(null);
    }

    private String nombreUbicacion(Integer idUbicacion) {
        return idUbicacion == null ? null : ubicacionRepository.findById(idUbicacion).map(UbicacionEntity::getNombre).orElse(null);
    }

    private String nombreSeveridad(Integer idSeveridad) {
        return idSeveridad == null ? null : severidadRepository.findById(idSeveridad).map(SeveridadEntity::getNombre).orElse(null);
    }

    private String nombrePrioridad(Integer idPrioridad) {
        return idPrioridad == null ? null : prioridadRepository.findById(idPrioridad).map(PrioridadEntity::getNombre).orElse(null);
    }

    private EstadoIncidenciaEntity obtenerEstado(int idEstado) {
        return estadoIncidenciaRepository.findById(idEstado)
            .orElseThrow(() -> new IllegalStateException("No existe el estado " + idEstado));
    }

    private void registrarHistorial(Integer idIncidencia, Integer idUsuario, String tipoEvento,
                                    String valorAnterior, String valorNuevo, String descripcion) {
        IncidenciaHistorialEntity historial = new IncidenciaHistorialEntity();
        historial.setIdIncidencia(idIncidencia);
        historial.setIdUsuario(idUsuario);
        historial.setTipoEvento(tipoEvento);
        historial.setValorAnterior(valorAnterior);
        historial.setValorNuevo(valorNuevo);
        historial.setDescripcion(descripcion);
        historial.setFechaEvento(LocalDateTime.now());
        historial.setActivo(Boolean.TRUE);
        incidenciaHistorialRepository.save(historial);
    }

    private void registrarCambioSiAplica(IncidenciaEntity incidencia, Integer idUsuario, String tipoEvento,
                                         Integer anterior, Integer nuevo, String descripcion) {
        if (anterior == null || !anterior.equals(nuevo)) {
            registrarHistorial(incidencia.getIdIncidencia(), idUsuario, tipoEvento,
                valor(anterior), valor(nuevo), descripcion);
        }
    }

    private boolean criterioValido(Integer valor, int minimo, int maximo) {
        return valor != null && valor >= minimo && valor <= maximo;
    }

    private int calcularReincidencia(IncidenciaEntity incidencia, LocalDateTime fechaReferencia) {
        long cantidad = incidenciaRepository.contarIncidenciasRelacionadas(
            incidencia.getIdCategoria(), incidencia.getIdUbicacion(),
            fechaReferencia.minusDays(ventanaReincidenciaDias), fechaReferencia);
        return Math.min(4, Math.max(1, (int) cantidad));
    }

    private void recalcularPrioridad(IncidenciaEntity incidencia, Integer idUsuario, LocalDateTime fechaReferencia) {
        int reincidencia = calcularReincidencia(incidencia, fechaReferencia);
        PrioridadCalculatorService.Resultado calculo = prioridadCalculatorService.calcular(
            incidencia.getImpacto(), incidencia.getUrgencia(), reincidencia);
        PrioridadEntity prioridad = prioridadRepository.findByPuntaje(calculo.puntuacion())
            .orElseThrow(() -> new IllegalStateException("No existe prioridad configurada para el puntaje " + calculo.puntuacion()));
        registrarAuditoriaPrioridad(incidencia, idUsuario, prioridad, calculo,
            incidencia.getImpacto(), incidencia.getUrgencia(), reincidencia);
        incidencia.setReincidencia(reincidencia);
        incidencia.setPuntajePrioridad(calculo.puntuacion());
        incidencia.setIdPrioridad(prioridad.getIdPrioridad());
    }

    private void registrarAuditoriaPrioridad(IncidenciaEntity incidencia, Integer idUsuario,
                                             PrioridadEntity prioridad,
                                             PrioridadCalculatorService.Resultado calculo,
                                             Integer impacto,
                                             Integer urgencia,
                                             int reincidencia) {
        boolean cambio = incidencia.getIdPrioridad() == null
            || !incidencia.getIdPrioridad().equals(prioridad.getIdPrioridad())
            || incidencia.getPuntajePrioridad() == null
            || incidencia.getPuntajePrioridad().compareTo(calculo.puntuacion()) != 0
            || incidencia.getReincidencia() == null
            || incidencia.getReincidencia() != reincidencia;
        if (cambio) {
            IncidenciaHistorialEntity historial = new IncidenciaHistorialEntity();
            historial.setIdIncidencia(incidencia.getIdIncidencia());
            historial.setIdUsuario(idUsuario);
            historial.setTipoEvento("CAMBIO_PRIORIDAD");
            historial.setValorAnterior(valor(incidencia.getIdPrioridad()));
            historial.setValorNuevo(valor(prioridad.getIdPrioridad()));
            historial.setImpacto(impacto);
            historial.setUrgencia(urgencia);
            historial.setReincidencia(reincidencia);
            historial.setPuntajePrioridad(calculo.puntuacion());
            historial.setDescripcion("Prioridad calculada. "
                + explicacion(calculo, impacto, urgencia, reincidencia));
            historial.setFechaEvento(LocalDateTime.now());
            historial.setActivo(Boolean.TRUE);
            incidenciaHistorialRepository.save(historial);
        }
    }

    private String explicacion(PrioridadCalculatorService.Resultado calculo, Integer impacto,
                               Integer urgencia, int reincidencia) {
        return "impacto=" + impacto + ", urgencia=" + urgencia + ", reincidencia=" + reincidencia
            + "; ponderaciones: impacto 40%, urgencia 40% y reincidencia 20%; puntuacion="
            + calculo.puntuacion().toPlainString() + "; prioridad=" + calculo.prioridad();
    }

    private String generarCodigo(LocalDateTime fecha) {
        return "INC-" + fecha.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    private String valor(Integer valor) {
        return valor == null ? null : valor.toString();
    }

    private ResponseEntity<Object> error(String mensaje) {
        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, mensaje);
    }

    private ResponseEntity<Object> errorForbidden(String mensaje) {
        return MessageResponse.setResponse(
                Boolean.FALSE,
                HttpStatus.FORBIDDEN,
                mensaje
        );
    }

    private ResponseEntity<Object> errorNotFound(String mensaje) {
        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, mensaje);
    }
}
