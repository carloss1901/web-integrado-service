package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import com.proyecto.integrador.model.projection.UsuarioProjection;
import com.proyecto.integrador.model.request.usuario.ActualizarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.ListarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.model.response.UsuarioResponse;
import com.proyecto.integrador.model.response.MaestroResponse;
import com.proyecto.integrador.model.response.UsuarioListadoResponse;
import com.proyecto.integrador.model.response.ResponsableResponse;
import com.proyecto.integrador.model.enums.RolEnum;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.service.UsuarioService;
import com.proyecto.integrador.util.CustomPage;
import com.proyecto.integrador.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final String PASSWORD_USUARIO_NUEVO = "campus360";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<UsuarioListadoResponse> listarUsuario(ListarUsuarioRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        var page = usuarioRepository.listarUsuario(request, pageable);
        CustomPage<UsuarioListadoResponse> response = new CustomPage<>(page.map(this::mapearUsuarioListado));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponsableResponse> listarResponsablesActivos() {
        return usuarioRepository.listarResponsablesActivos(RolEnum.RESPONSABLE_ATENCION.getId());
    }

    private UsuarioListadoResponse mapearUsuarioListado(UsuarioProjection projection) {
        UsuarioListadoResponse response = new UsuarioListadoResponse();
        response.setIdUsuario(projection.getIdUsuario());
        response.setUsuario(projection.getUsuario());
        response.setNombres(projection.getNombres());
        response.setApePaterno(projection.getApePaterno());
        response.setApeMaterno(projection.getApeMaterno());
        response.setCorreo(projection.getCorreo());
        response.setActivo(projection.getActivo());
        response.setEstadoDsc(projection.getEstadoDsc());
        response.setFecRegistro(projection.getFecRegistro());
        response.setFecModificacion(projection.getFecModificacion());
        response.setRoles(usuarioRolRepository.listarIdsRolesPorUsuario(projection.getIdUsuario())
                .stream()
                .map(idRol -> rolRepository.findById(idRol).orElse(null))
                .filter(rol -> rol != null && Boolean.TRUE.equals(rol.getActivo()))
                .map(rol -> {
                    MaestroResponse item = new MaestroResponse();
                    item.setMaestroId(rol.getIdRol());
                    item.setDescripcion(rol.getNombre());
                    return item;
                })
                .toList());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            return error("El idUsuario es obligatorio");
        }

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null || Boolean.FALSE.equals(usuario.getActivo())) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, "El usuario no existe");
        }

        UsuarioResponse response = new UsuarioResponse();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setUsuario(usuario.getUsuario());
        response.setNombres(usuario.getNombres());
        response.setApePaterno(usuario.getApePaterno());
        response.setApeMaterno(usuario.getApeMaterno());
        response.setCorreo(usuario.getCorreo());
        response.setEstado(usuario.getEstado());
        response.setActivo(usuario.getActivo());
        response.setIdsRoles(usuarioRolRepository.listarIdsRolesPorUsuario(usuario.getIdUsuario()));

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Proceso exitoso", response);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarUsuario(RegistrarUsuarioRequest request) {
        if (request.getUsuario() == null || request.getUsuario().isBlank()) {
            return error("El usuario es obligatorio");
        }
        if (usuarioRepository.existsByUsuarioIgnoreCase(request.getUsuario())) {
            return error("El usuario ya existe");
        }
        if (usuarioRepository.existsByCorreoIgnoreCase(request.getCorreo())) {
            return error("El correo ya existe");
        }
        Set<Integer> idsRoles = request.getIdsRoles() == null ? new HashSet<>() : new HashSet<>(request.getIdsRoles());
        if (idsRoles.contains(null)) {
            return error("Los roles no pueden ser nulos");
        }
        if (!idsRoles.isEmpty()) {
            for (Integer idRol : idsRoles) {
                if (!rolRepository.existsById(idRol)) {
                    return error("El rol " + idRol + " no existe");
                }
            }
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUsuario(request.getUsuario());
        usuario.setNombres(request.getNombres());
        usuario.setApePaterno(request.getApePaterno());
        usuario.setApeMaterno(request.getApeMaterno());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(PASSWORD_USUARIO_NUEVO));
        usuario.setEstado(Boolean.TRUE);
        usuario.setActivo(Boolean.TRUE);
        usuarioRepository.save(usuario);

        if (!idsRoles.isEmpty()) {
            for (Integer idRol : idsRoles) {
                UsuarioRolEntity usuarioRol = new UsuarioRolEntity();
                usuarioRol.setIdUsuario(usuario.getIdUsuario());
                usuarioRol.setIdRol(idRol);
                usuarioRol.setEstado(Boolean.TRUE);
                usuarioRol.setActivo(Boolean.TRUE);
                usuarioRolRepository.save(usuarioRol);
            }
        }

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Usuario registrado correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> actualizarUsuario(ActualizarUsuarioRequest request) {
        if (request.getIdUsuario() == null) {
            return error("El idUsuario es obligatorio");
        }
        if (usuarioRepository.countByCorreoAndIdUsuarioNot(request.getCorreo(), request.getIdUsuario()) > 0) {
            return error("El correo ya existe");
        }

        boolean actualizarRoles = request.getIdsRoles() != null;
        Set<Integer> idsRoles = new HashSet<>();
        if (actualizarRoles) {
            idsRoles = obtenerRolesValidados(request);
            if (idsRoles == null) {
                return error("Los roles no pueden ser nulos");
            }
            ResponseEntity<Object> validacionRoles = validarRolesExistentes(idsRoles);
            if (validacionRoles != null) {
                return validacionRoles;
            }
        }

        UsuarioEntity usuario = usuarioRepository.findById(request.getIdUsuario()).orElse(null);
        if (usuario == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, "El usuario no existe");
        }

        usuario.setNombres(request.getNombres());
        usuario.setApePaterno(request.getApePaterno());
        usuario.setApeMaterno(request.getApeMaterno());
        usuario.setCorreo(request.getCorreo());
        usuario.setFecMod(LocalDateTime.now());
        usuarioRepository.save(usuario);

        if (actualizarRoles) {
            actualizarRoles(usuario.getIdUsuario(), idsRoles);
        }

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Usuario actualizado correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> eliminarUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            return error("El idUsuario es obligatorio");
        }

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, "El usuario no existe");
        }

        usuario.setActivo(Boolean.FALSE);
        usuario.setEstado(Boolean.FALSE);
        usuario.setFecMod(LocalDateTime.now());
        usuarioRepository.save(usuario);
        usuarioRolRepository.desactivarByIdUsuario(usuario.getIdUsuario());

        Map<String, Object> data = new HashMap<>();
        data.put("idUsuario", usuario.getIdUsuario());
        data.put("usuario", usuario.getUsuario());
        data.put("activo", usuario.getActivo());

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Usuario eliminado correctamente", data);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> activarUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            return error("El idUsuario es obligatorio");
        }

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND, "El usuario no existe");
        }

        usuario.setActivo(Boolean.TRUE);
        usuario.setEstado(Boolean.TRUE);
        usuario.setFecMod(LocalDateTime.now());
        usuarioRepository.save(usuario);

        Map<String, Object> data = new HashMap<>();
        data.put("idUsuario", usuario.getIdUsuario());
        data.put("usuario", usuario.getUsuario());
        data.put("activo", usuario.getActivo());

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Usuario activado correctamente", data);
    }

    private Set<Integer> obtenerRolesValidados(ActualizarUsuarioRequest request) {
        Set<Integer> idsRoles = request.getIdsRoles() == null ? new HashSet<>() : new HashSet<>(request.getIdsRoles());
        if (idsRoles.contains(null)) {
            return null;
        }
        return idsRoles;
    }

    private ResponseEntity<Object> validarRolesExistentes(Set<Integer> idsRoles) {
        for (Integer idRol : idsRoles) {
            if (!rolRepository.existsById(idRol)) {
                return error("El rol " + idRol + " no existe");
            }
        }
        return null;
    }

    private void registrarRoles(Integer idUsuario, Set<Integer> idsRoles) {
        for (Integer idRol : idsRoles) {
            usuarioRolRepository.save(crearUsuarioRol(idUsuario, idRol));
        }
    }

    private void actualizarRoles(Integer idUsuario, Set<Integer> idsRoles) {
        List<UsuarioRolEntity> rolesExistentes = usuarioRolRepository.findByIdUsuario(idUsuario);
        Set<Integer> idsRolesExistentes = new HashSet<>();
        LocalDateTime ahora = LocalDateTime.now();

        for (UsuarioRolEntity usuarioRol : rolesExistentes) {
            idsRolesExistentes.add(usuarioRol.getIdRol());
            boolean asignado = idsRoles.contains(usuarioRol.getIdRol());
            usuarioRol.setActivo(asignado);
            usuarioRol.setEstado(asignado);
            usuarioRol.setFecMod(ahora);
        }
        usuarioRolRepository.saveAll(rolesExistentes);

        Set<Integer> idsNuevos = new HashSet<>(idsRoles);
        idsNuevos.removeAll(idsRolesExistentes);
        registrarRoles(idUsuario, idsNuevos);
    }

    private UsuarioRolEntity crearUsuarioRol(Integer idUsuario, Integer idRol) {
        UsuarioRolEntity usuarioRol = new UsuarioRolEntity();
        usuarioRol.setIdUsuario(idUsuario);
        usuarioRol.setIdRol(idRol);
        usuarioRol.setEstado(Boolean.TRUE);
        usuarioRol.setActivo(Boolean.TRUE);
        return usuarioRol;
    }

    private ResponseEntity<Object> error(String mensaje) {
        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, mensaje);
    }
}
