package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.entity.UsuarioRolEntity;
import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.service.UsuarioService;
import com.proyecto.integrador.util.MessageResponse;
import com.proyecto.integrador.util.PasswordHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
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
        usuarioRepository.save(usuario);

        if (!idsRoles.isEmpty()) {
            for (Integer idRol : idsRoles) {
                UsuarioRolEntity usuarioRol = new UsuarioRolEntity();
                usuarioRol.setIdUsuario(usuario.getIdUsuario());
                usuarioRol.setIdRol(idRol);
                usuarioRol.setEstado(Boolean.TRUE);
                usuarioRolRepository.save(usuarioRol);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("idUsuario", usuario.getIdUsuario());
        data.put("usuario", usuario.getUsuario());
        data.put("correo", usuario.getCorreo());
        data.put("idsRoles", idsRoles);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Usuario registrado correctamente", data);
    }

    private ResponseEntity<Object> error(String mensaje) {
        return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, mensaje);
    }
}
