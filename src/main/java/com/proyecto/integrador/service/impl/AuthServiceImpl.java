package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.request.auth.LoginRequest;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.security.JwtUtil;
import com.proyecto.integrador.service.AuthService;
import com.proyecto.integrador.util.MessageResponse;
import com.proyecto.integrador.util.PasswordHashUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolRepository rolRepository;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           UsuarioRolRepository usuarioRolRepository,
                           RolRepository rolRepository,
                           JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.rolRepository = rolRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<Object> login(LoginRequest request) {
        String usuario = request.getUsuario() == null ? null : request.getUsuario().trim();
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByUsuarioIgnoreCase(usuario);
        if (usuarioOpt.isEmpty()) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED, "Usuario o contrasena incorrectos", null);
        }
        UsuarioEntity usuarioEntity = usuarioOpt.get();
        if (Boolean.FALSE.equals(usuarioEntity.getEstado()) || Boolean.FALSE.equals(usuarioEntity.getActivo())) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED, "Usuario inactivo", null);
        }
        String hash = PasswordHashUtil.hash(request.getContrasena());
        if (!hash.equalsIgnoreCase(usuarioEntity.getPassword())) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED, "Usuario o contrasena incorrectos", null);
        }
        List<Integer> idRoles = usuarioRolRepository.listarIdsRolesPorUsuario(usuarioEntity.getIdUsuario());
        List<String> roles = rolRepository.findAllById(idRoles).stream()
            .map(RolEntity::getNombre)
            .sorted(Comparator.naturalOrder())
            .toList();
        String token = jwtUtil.generateToken(usuarioEntity.getIdUsuario(), usuarioEntity.getUsuario(),
            usuarioEntity.getNombres(), usuarioEntity.getApellidos(), roles);
        return MessageResponse.setResponse(true, HttpStatus.OK, "Login exitoso", token);
    }
}