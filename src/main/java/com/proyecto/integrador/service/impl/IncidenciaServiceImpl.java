package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.mapper.GenericMapper;
import com.proyecto.integrador.model.entity.EstadoIncidenciaEntity;
import com.proyecto.integrador.model.entity.IncidenciaEntity;
import com.proyecto.integrador.model.entity.IncidenciaEvidenciaEntity;
import com.proyecto.integrador.model.entity.IncidenciaHistorialEntity;
import com.proyecto.integrador.model.entity.PrioridadEntity;
import com.proyecto.integrador.model.entity.SlaEntity;
import com.proyecto.integrador.model.request.incidencia.ActualizarEstadoIncidenciaRequest;
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
import com.proyecto.integrador.service.IncidenciaService;
import com.proyecto.integrador.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class IncidenciaServiceImpl implements IncidenciaService {

    private static final String ROL_REPORTANTE = "REPORTANTE";
    private static final String ROL_OPERADOR = "OPERADOR_MESA_CONTROL";
    private static final String ROL_RESPONSABLE = "RESPONSABLE_ATENCION";
    private static final String ROL_SUPERVISOR = "SUPERVISOR";

    private static final String ESTADO_REGISTRADA = "REGISTRADA";
    private static final String ESTADO_CLASIFICADA = "CLASIFICADA";
    private static final String ESTADO_ASIGNADA = "ASIGNADA";
    private static final String ESTADO_EN_ATENCION = "EN_ATENCION";
    private static final String ESTADO_RESUELTA = "RESUELTA";
    private static final String ESTADO_CERRADA = "CERRADA";

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    @Autowired
    private IncidenciaHistorialRepository incidenciaHistorialRepository;

    @Autowired
    private IncidenciaEvidenciaRepository incidenciaEvidenciaRepository;

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

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaResponse> listarIncidencias() {
        return genericMapper.toResponseList(incidenciaRepository.listarIncidencias(), IncidenciaResponse.class);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarIncidencia(RegistrarIncidenciaRequest request) {
        if (!tieneRol(request.getIdReportante(), ROL_REPORTANTE)) {
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
        incidencia.setIdReportante(request.getIdReportante());
        incidencia.setIdCategoria(request.getIdCategoria());
        incidencia.setIdUbicacion(request.getIdUbicacion());
        incidencia.setIdEstado(obtenerEstado(ESTADO_REGISTRADA).getIdEstado());
        incidencia.setTitulo(request.getTitulo());
        incidencia.setDescripcion(request.getDescripcion());
        incidencia.setFechaRegistro(ahora);

        incidenciaRepository.save(incidencia);
        registrarHistorial(incidencia.getIdIncidencia(), request.getIdReportante(), "REGISTRO_INCIDENCIA",
            null, incidencia.getCodigo(), "Registro inicial de incidencia");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia registrada correctamente", incidencia);
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
        if (!criterioValido(request.getImpacto(), 1, 3)
            || !criterioValido(request.getUrgencia(), 1, 3)
            || !criterioValido(request.getReincidencia(), 0, 1)) {
            return error("Impacto, urgencia o reincidencia tienen valores invalidos");
        }

        IncidenciaEntity incidencia = obtenerIncidencia(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }

        Integer puntaje = request.getImpacto() + request.getUrgencia() + request.getReincidencia();
        PrioridadEntity prioridad = prioridadRepository.findByPuntaje(puntaje)
            .orElseThrow(() -> new IllegalStateException("No existe prioridad configurada para el puntaje " + puntaje));
        SlaEntity sla = slaRepository.findByIdSeveridad(request.getIdSeveridad())
            .orElseThrow(() -> new IllegalStateException("No existe SLA configurado para la severidad"));

        registrarCambioSiAplica(incidencia, request.getIdUsuario(), "CAMBIO_SEVERIDAD",
            incidencia.getIdSeveridad(), request.getIdSeveridad(), "Clasificacion de severidad");
        registrarCambioSiAplica(incidencia, request.getIdUsuario(), "CAMBIO_PRIORIDAD",
            incidencia.getIdPrioridad(), prioridad.getIdPrioridad(), "Determinacion automatica de prioridad");

        incidencia.setIdSeveridad(request.getIdSeveridad());
        incidencia.setImpacto(request.getImpacto());
        incidencia.setUrgencia(request.getUrgencia());
        incidencia.setReincidencia(request.getReincidencia());
        incidencia.setPuntajePrioridad(puntaje);
        incidencia.setIdPrioridad(prioridad.getIdPrioridad());
        incidencia.setIdSla(sla.getIdSla());
        incidencia.setFechaLimite(incidencia.getFechaRegistro().plusMinutes(sla.getMinutosResolucion()));
        incidencia.setIdEstado(obtenerEstado(ESTADO_CLASIFICADA).getIdEstado());
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);

        registrarHistorial(incidencia.getIdIncidencia(), request.getIdUsuario(), "CLASIFICACION",
            null, puntaje.toString(), "Incidencia clasificada y priorizada");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia clasificada correctamente", incidencia);
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

        IncidenciaEntity incidencia = obtenerIncidencia(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }

        Integer responsableAnterior = incidencia.getIdResponsable();
        incidencia.setIdResponsable(request.getIdResponsable());
        incidencia.setIdEstado(obtenerEstado(ESTADO_ASIGNADA).getIdEstado());
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);

        registrarHistorial(incidencia.getIdIncidencia(), request.getIdUsuario(), "ASIGNACION_RESPONSABLE",
            valor(responsableAnterior), request.getIdResponsable().toString(), "Asignacion de responsable de atencion");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Responsable asignado correctamente", incidencia);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> iniciarAtencion(ActualizarEstadoIncidenciaRequest request) {
        return cambiarEstadoPorResponsable(request, ESTADO_EN_ATENCION, "INICIO_ATENCION",
            "Inicio de atencion de incidencia", false, false);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> resolverIncidencia(ActualizarEstadoIncidenciaRequest request) {
        return cambiarEstadoPorResponsable(request, ESTADO_RESUELTA, "RESOLUCION",
            "Resolucion de incidencia", true, false);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cerrarIncidencia(ActualizarEstadoIncidenciaRequest request) {
        if (!tieneRol(request.getIdUsuario(), ROL_SUPERVISOR) && !tieneRol(request.getIdUsuario(), ROL_REPORTANTE)) {
            return error("El usuario no tiene rol autorizado para cerrar incidencias");
        }

        IncidenciaEntity incidencia = obtenerIncidencia(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }

        String estadoAnterior = valor(incidencia.getIdEstado());
        incidencia.setIdEstado(obtenerEstado(ESTADO_CERRADA).getIdEstado());
        incidencia.setFechaCierre(LocalDateTime.now());
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);

        registrarHistorial(incidencia.getIdIncidencia(), request.getIdUsuario(), "CIERRE",
            estadoAnterior, incidencia.getIdEstado().toString(), "Cierre definitivo de incidencia");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia cerrada correctamente", incidencia);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarEvidencia(RegistrarEvidenciaRequest request) {
        IncidenciaEntity incidencia = obtenerIncidencia(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        if (!usuarioRepository.existsById(request.getIdUsuario())) {
            return error("El usuario no existe");
        }

        IncidenciaEvidenciaEntity evidencia = new IncidenciaEvidenciaEntity();
        evidencia.setIdIncidencia(request.getIdIncidencia());
        evidencia.setIdUsuario(request.getIdUsuario());
        evidencia.setNombreArchivo(request.getNombreArchivo());
        evidencia.setRutaArchivo(request.getRutaArchivo());
        evidencia.setDescripcion(request.getDescripcion());
        evidencia.setFechaRegistro(LocalDateTime.now());
        incidenciaEvidenciaRepository.save(evidencia);

        registrarHistorial(request.getIdIncidencia(), request.getIdUsuario(), "REGISTRO_EVIDENCIA",
            null, request.getNombreArchivo(), "Registro de evidencia de incidencia");

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Evidencia registrada correctamente", evidencia);
    }

    private ResponseEntity<Object> cambiarEstadoPorResponsable(
        ActualizarEstadoIncidenciaRequest request,
        String estadoNuevo,
        String tipoEvento,
        String descripcion,
        boolean registrarResolucion,
        boolean registrarCierre
    ) {
        if (!tieneRol(request.getIdUsuario(), ROL_RESPONSABLE)) {
            return error("El usuario no tiene rol RESPONSABLE_ATENCION");
        }

        IncidenciaEntity incidencia = obtenerIncidencia(request.getIdIncidencia());
        if (incidencia == null) {
            return errorNotFound("La incidencia no existe");
        }
        if (!request.getIdUsuario().equals(incidencia.getIdResponsable())) {
            return error("La incidencia no esta asignada al usuario indicado");
        }

        String estadoAnterior = valor(incidencia.getIdEstado());
        incidencia.setIdEstado(obtenerEstado(estadoNuevo).getIdEstado());
        if (registrarResolucion) {
            incidencia.setFechaResolucion(LocalDateTime.now());
        }
        if (registrarCierre) {
            incidencia.setFechaCierre(LocalDateTime.now());
        }
        incidencia.setFecMod(LocalDateTime.now());
        incidenciaRepository.save(incidencia);

        registrarHistorial(incidencia.getIdIncidencia(), request.getIdUsuario(), tipoEvento,
            estadoAnterior, incidencia.getIdEstado().toString(), descripcion);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, descripcion, incidencia);
    }

    private boolean tieneRol(Integer idUsuario, String nombreRol) {
        return idUsuario != null && usuarioRepository.countUsuarioByRol(idUsuario, nombreRol) > 0;
    }

    private IncidenciaEntity obtenerIncidencia(Integer idIncidencia) {
        if (idIncidencia == null) {
            return null;
        }
        return incidenciaRepository.findById(idIncidencia).orElse(null);
    }

    private EstadoIncidenciaEntity obtenerEstado(String nombreEstado) {
        return estadoIncidenciaRepository.findByNombreIgnoreCase(nombreEstado)
            .orElseThrow(() -> new IllegalStateException("No existe el estado " + nombreEstado));
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

    private String generarCodigo(LocalDateTime fecha) {
        return "INC-" + fecha.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    private String valor(Integer valor) {
        return valor == null ? null : valor.toString();
    }

    private ResponseEntity<Object> error(String mensaje) {
        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, mensaje);
    }

    private ResponseEntity<Object> errorNotFound(String mensaje) {
        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, mensaje);
    }
}
