package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import com.proyecto.integrador.model.projection.UsuarioProjection;
import com.proyecto.integrador.model.request.usuario.ActualizarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.ListarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.model.response.UsuarioResponse;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.service.UsuarioService;
import com.proyecto.integrador.util.CustomPage;
import com.proyecto.integrador.util.MessageResponse;
import com.proyecto.integrador.util.PasswordHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<UsuarioProjection> listarUsuario(ListarUsuarioRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        return new CustomPage<>(usuarioRepository.listarUsuario(request, pageable));
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
        response.setApellidos(usuario.getApellidos());
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
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return error("La contrasenia es obligatoria");
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
        usuario.setApellidos(request.getApellidos());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(PasswordHashUtil.hash(request.getPassword()));
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
        usuario.setApellidos(request.getApellidos());
        usuario.setCorreo(request.getCorreo());
        usuario.setFecMod(LocalDateTime.now());
        usuarioRepository.save(usuario);

        if (actualizarRoles) {
            usuarioRolRepository.deleteByIdUsuario(usuario.getIdUsuario());
            registrarRoles(usuario.getIdUsuario(), idsRoles);
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
            UsuarioRolEntity usuarioRol = new UsuarioRolEntity();
            usuarioRol.setIdUsuario(idUsuario);
            usuarioRol.setIdRol(idRol);
            usuarioRol.setEstado(Boolean.TRUE);
            usuarioRol.setActivo(Boolean.TRUE);
            usuarioRolRepository.save(usuarioRol);
        }
    }

    private ResponseEntity<Object> error(String mensaje) {
        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, mensaje);
    }
}
