package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.mapper.GenericMapper;
import com.proyecto.integrador.model.enums.TipoMaestro;
import com.proyecto.integrador.model.entity.CategoriaEntity;
import com.proyecto.integrador.model.entity.EstadoIncidenciaEntity;
import com.proyecto.integrador.model.entity.PrioridadEntity;
import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.entity.SeveridadEntity;
import com.proyecto.integrador.model.entity.SlaEntity;
import com.proyecto.integrador.model.entity.UbicacionEntity;
import com.proyecto.integrador.model.request.comunes.MantenerMaestroRequest;
import com.proyecto.integrador.model.response.MaestroResponse;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.repository.ComunesRepository;
import com.proyecto.integrador.repository.EstadoIncidenciaRepository;
import com.proyecto.integrador.repository.PrioridadRepository;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.SeveridadRepository;
import com.proyecto.integrador.repository.SlaRepository;
import com.proyecto.integrador.repository.UbicacionRepository;
import com.proyecto.integrador.service.ComunesService;
import com.proyecto.integrador.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComunesServiceImpl implements ComunesService {

    @Autowired
    private ComunesRepository comunesRepository;

    @Autowired
    private GenericMapper genericMapper;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private SeveridadRepository severidadRepository;

    @Autowired
    private PrioridadRepository prioridadRepository;

    @Autowired
    private EstadoIncidenciaRepository estadoIncidenciaRepository;

    @Autowired
    private SlaRepository slaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarRoles() {
        return genericMapper.toResponseList(comunesRepository.listarRoles(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarCategorias() {
        return genericMapper.toResponseList(comunesRepository.listarCategorias(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarUbicaciones() {
        return genericMapper.toResponseList(comunesRepository.listarUbicaciones(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarSeveridades() {
        return genericMapper.toResponseList(comunesRepository.listarSeveridades(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarPrioridades() {
        return genericMapper.toResponseList(comunesRepository.listarPrioridades(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarEstadosIncidencia() {
        return genericMapper.toResponseList(comunesRepository.listarEstadosIncidencia(), MaestroResponse.class);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarMaestro(TipoMaestro maestro, MantenerMaestroRequest request) {
        try {
            switch (maestro) {
                case ROLES -> registrarRol(request);
                case CATEGORIAS -> registrarCategoria(request);
                case UBICACIONES -> registrarUbicacion(request);
                case SEVERIDADES -> registrarSeveridad(request);
                case PRIORIDADES -> registrarPrioridad(request);
                case ESTADOS_INCIDENCIA -> registrarEstadoIncidencia(request);
                case SLA -> registrarSla(request);
            }
        } catch (IllegalArgumentException ex) {
            return error(ex.getMessage());
        }
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Registro creado correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> actualizarMaestro(TipoMaestro maestro, MantenerMaestroRequest request) {
        if (request.getIdMaestro() == null) {
            return error("El idMaestro es obligatorio");
        }

        try {
            switch (maestro) {
                case ROLES -> actualizarRol(request);
                case CATEGORIAS -> actualizarCategoria(request);
                case UBICACIONES -> actualizarUbicacion(request);
                case SEVERIDADES -> actualizarSeveridad(request);
                case PRIORIDADES -> actualizarPrioridad(request);
                case ESTADOS_INCIDENCIA -> actualizarEstadoIncidencia(request);
                case SLA -> actualizarSla(request);
            }
        } catch (IllegalArgumentException ex) {
            return error(ex.getMessage());
        }
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Registro actualizado correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> eliminarMaestro(TipoMaestro maestro, Integer idMaestro) {
        if (idMaestro == null) {
            return error("El idMaestro es obligatorio");
        }

        try {
            switch (maestro) {
                case ROLES -> eliminarRol(idMaestro);
                case CATEGORIAS -> eliminarCategoria(idMaestro);
                case UBICACIONES -> eliminarUbicacion(idMaestro);
                case SEVERIDADES -> eliminarSeveridad(idMaestro);
                case PRIORIDADES -> eliminarPrioridad(idMaestro);
                case ESTADOS_INCIDENCIA -> eliminarEstadoIncidencia(idMaestro);
                case SLA -> eliminarSla(idMaestro);
            }
        } catch (IllegalArgumentException ex) {
            return error(ex.getMessage());
        }
        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Registro eliminado correctamente");
    }

    private void registrarRol(MantenerMaestroRequest request) {
        validarDescripcion(request);
        RolEntity entity = new RolEntity();
        entity.setNombre(request.getDescripcion());
        entity.setActivo(Boolean.TRUE);
        rolRepository.save(entity);
    }

    private void actualizarRol(MantenerMaestroRequest request) {
        RolEntity entity = rolRepository.findById(request.getIdMaestro())
            .orElseThrow(() -> new IllegalArgumentException("El rol no existe"));
        validarDescripcion(request);
        entity.setNombre(request.getDescripcion());
        marcarModificado(entity);
        rolRepository.save(entity);
    }

    private void eliminarRol(Integer idMaestro) {
        RolEntity entity = rolRepository.findById(idMaestro)
            .orElseThrow(() -> new IllegalArgumentException("El rol no existe"));
        entity.setActivo(Boolean.FALSE);
        marcarModificado(entity);
        rolRepository.save(entity);
    }

    private void registrarCategoria(MantenerMaestroRequest request) {
        validarDescripcion(request);
        CategoriaEntity entity = new CategoriaEntity();
        entity.setNombre(request.getDescripcion());
        entity.setDescripcion(request.getDetalle());
        entity.setActivo(Boolean.TRUE);
        categoriaRepository.save(entity);
    }

    private void actualizarCategoria(MantenerMaestroRequest request) {
        CategoriaEntity entity = categoriaRepository.findById(request.getIdMaestro())
            .orElseThrow(() -> new IllegalArgumentException("La categoria no existe"));
        validarDescripcion(request);
        entity.setNombre(request.getDescripcion());
        entity.setDescripcion(request.getDetalle());
        entity.setFecMod(LocalDateTime.now());
        categoriaRepository.save(entity);
    }

    private void eliminarCategoria(Integer idMaestro) {
        CategoriaEntity entity = categoriaRepository.findById(idMaestro)
            .orElseThrow(() -> new IllegalArgumentException("La categoria no existe"));
        entity.setActivo(Boolean.FALSE);
        entity.setFecMod(LocalDateTime.now());
        categoriaRepository.save(entity);
    }

    private void registrarUbicacion(MantenerMaestroRequest request) {
        validarDescripcion(request);
        UbicacionEntity entity = new UbicacionEntity();
        entity.setNombre(request.getDescripcion());
        entity.setDescripcion(request.getDetalle());
        entity.setActivo(Boolean.TRUE);
        ubicacionRepository.save(entity);
    }

    private void actualizarUbicacion(MantenerMaestroRequest request) {
        UbicacionEntity entity = ubicacionRepository.findById(request.getIdMaestro())
            .orElseThrow(() -> new IllegalArgumentException("La ubicacion no existe"));
        validarDescripcion(request);
        entity.setNombre(request.getDescripcion());
        entity.setDescripcion(request.getDetalle());
        entity.setFecMod(LocalDateTime.now());
        ubicacionRepository.save(entity);
    }

    private void eliminarUbicacion(Integer idMaestro) {
        UbicacionEntity entity = ubicacionRepository.findById(idMaestro)
            .orElseThrow(() -> new IllegalArgumentException("La ubicacion no existe"));
        entity.setActivo(Boolean.FALSE);
        entity.setFecMod(LocalDateTime.now());
        ubicacionRepository.save(entity);
    }

    private void registrarSeveridad(MantenerMaestroRequest request) {
        validarDescripcion(request);
        validarObligatorio(request.getNivel(), "El nivel es obligatorio");
        SeveridadEntity entity = new SeveridadEntity();
        entity.setNombre(request.getDescripcion());
        entity.setNivel(request.getNivel());
        entity.setActivo(Boolean.TRUE);
        severidadRepository.save(entity);
    }

    private void actualizarSeveridad(MantenerMaestroRequest request) {
        SeveridadEntity entity = severidadRepository.findById(request.getIdMaestro())
            .orElseThrow(() -> new IllegalArgumentException("La severidad no existe"));
        validarDescripcion(request);
        validarObligatorio(request.getNivel(), "El nivel es obligatorio");
        entity.setNombre(request.getDescripcion());
        entity.setNivel(request.getNivel());
        entity.setFecMod(LocalDateTime.now());
        severidadRepository.save(entity);
    }

    private void eliminarSeveridad(Integer idMaestro) {
        SeveridadEntity entity = severidadRepository.findById(idMaestro)
            .orElseThrow(() -> new IllegalArgumentException("La severidad no existe"));
        entity.setActivo(Boolean.FALSE);
        entity.setFecMod(LocalDateTime.now());
        severidadRepository.save(entity);
    }

    private void registrarPrioridad(MantenerMaestroRequest request) {
        validarDescripcion(request);
        validarObligatorio(request.getNivel(), "El nivel es obligatorio");
        validarObligatorio(request.getPuntajeMinimo(), "El puntaje minimo es obligatorio");
        validarObligatorio(request.getPuntajeMaximo(), "El puntaje maximo es obligatorio");
        PrioridadEntity entity = new PrioridadEntity();
        entity.setNombre(request.getDescripcion());
        entity.setNivel(request.getNivel());
        entity.setPuntajeMinimo(request.getPuntajeMinimo());
        entity.setPuntajeMaximo(request.getPuntajeMaximo());
        entity.setActivo(Boolean.TRUE);
        prioridadRepository.save(entity);
    }

    private void actualizarPrioridad(MantenerMaestroRequest request) {
        PrioridadEntity entity = prioridadRepository.findById(request.getIdMaestro())
            .orElseThrow(() -> new IllegalArgumentException("La prioridad no existe"));
        validarDescripcion(request);
        validarObligatorio(request.getNivel(), "El nivel es obligatorio");
        validarObligatorio(request.getPuntajeMinimo(), "El puntaje minimo es obligatorio");
        validarObligatorio(request.getPuntajeMaximo(), "El puntaje maximo es obligatorio");
        entity.setNombre(request.getDescripcion());
        entity.setNivel(request.getNivel());
        entity.setPuntajeMinimo(request.getPuntajeMinimo());
        entity.setPuntajeMaximo(request.getPuntajeMaximo());
        entity.setFecMod(LocalDateTime.now());
        prioridadRepository.save(entity);
    }

    private void eliminarPrioridad(Integer idMaestro) {
        PrioridadEntity entity = prioridadRepository.findById(idMaestro)
            .orElseThrow(() -> new IllegalArgumentException("La prioridad no existe"));
        entity.setActivo(Boolean.FALSE);
        entity.setFecMod(LocalDateTime.now());
        prioridadRepository.save(entity);
    }

    private void registrarEstadoIncidencia(MantenerMaestroRequest request) {
        validarDescripcion(request);
        EstadoIncidenciaEntity entity = new EstadoIncidenciaEntity();
        entity.setNombre(request.getDescripcion());
        entity.setActivo(Boolean.TRUE);
        estadoIncidenciaRepository.save(entity);
    }

    private void actualizarEstadoIncidencia(MantenerMaestroRequest request) {
        EstadoIncidenciaEntity entity = estadoIncidenciaRepository.findById(request.getIdMaestro())
            .orElseThrow(() -> new IllegalArgumentException("El estado de incidencia no existe"));
        validarDescripcion(request);
        entity.setNombre(request.getDescripcion());
        entity.setFecMod(LocalDateTime.now());
        estadoIncidenciaRepository.save(entity);
    }

    private void eliminarEstadoIncidencia(Integer idMaestro) {
        EstadoIncidenciaEntity entity = estadoIncidenciaRepository.findById(idMaestro)
            .orElseThrow(() -> new IllegalArgumentException("El estado de incidencia no existe"));
        entity.setActivo(Boolean.FALSE);
        entity.setFecMod(LocalDateTime.now());
        estadoIncidenciaRepository.save(entity);
    }

    private void registrarSla(MantenerMaestroRequest request) {
        validarObligatorio(request.getIdSeveridad(), "La severidad es obligatoria");
        validarObligatorio(request.getMinutosResolucion(), "Los minutos de resolucion son obligatorios");
        SlaEntity entity = new SlaEntity();
        entity.setIdSeveridad(request.getIdSeveridad());
        entity.setMinutosResolucion(request.getMinutosResolucion());
        entity.setActivo(Boolean.TRUE);
        slaRepository.save(entity);
    }

    private void actualizarSla(MantenerMaestroRequest request) {
        SlaEntity entity = slaRepository.findById(request.getIdMaestro())
            .orElseThrow(() -> new IllegalArgumentException("El SLA no existe"));
        validarObligatorio(request.getIdSeveridad(), "La severidad es obligatoria");
        validarObligatorio(request.getMinutosResolucion(), "Los minutos de resolucion son obligatorios");
        entity.setIdSeveridad(request.getIdSeveridad());
        entity.setMinutosResolucion(request.getMinutosResolucion());
        entity.setFecMod(LocalDateTime.now());
        slaRepository.save(entity);
    }

    private void eliminarSla(Integer idMaestro) {
        SlaEntity entity = slaRepository.findById(idMaestro)
            .orElseThrow(() -> new IllegalArgumentException("El SLA no existe"));
        entity.setActivo(Boolean.FALSE);
        entity.setFecMod(LocalDateTime.now());
        slaRepository.save(entity);
    }

    private void marcarModificado(RolEntity entity) {
        entity.setFecMod(LocalDateTime.now());
    }

    private void validarDescripcion(MantenerMaestroRequest request) {
        if (request.getDescripcion() == null || request.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion es obligatoria");
        }
    }

    private void validarObligatorio(Integer value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private ResponseEntity<Object> error(String mensaje) {
        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, mensaje);
    }
}
