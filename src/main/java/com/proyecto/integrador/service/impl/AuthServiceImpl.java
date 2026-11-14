package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.enums.RolEnum;
import com.proyecto.integrador.model.request.auth.LoginRequest;
import com.proyecto.integrador.model.request.auth.SeleccionarRolRequest;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.security.JwtUtil;
import com.proyecto.integrador.security.JwtRole;
import com.proyecto.integrador.service.AuthService;
import com.proyecto.integrador.util.MessageResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${app.auth.bypass-password:false}")
    private boolean bypassPassword;

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final RolRepository rolRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            UsuarioRolRepository usuarioRolRepository,
            RolRepository rolRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.rolRepository = rolRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<Object> login(LoginRequest request) {
        String usuario = request.getUsuario() == null ? null : request.getUsuario().trim();
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByUsuarioIgnoreCase(usuario);
        if (usuarioOpt.isEmpty()) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED, "Usuario o contrasena incorrectos", null);
        }
        UsuarioEntity usuarioEntity = usuarioOpt.get();
        if (Boolean.FALSE.equals(usuarioEntity.getEstado())
                || Boolean.FALSE.equals(usuarioEntity.getActivo())) {
            return MessageResponse.setResponse(
                    false,
                    HttpStatus.UNAUTHORIZED,
                    "Usuario o contrasena incorrectos",
                    null
            );
        }

        if (!bypassPassword && !passwordEncoder.matches(
                request.getContrasena(),
                usuarioEntity.getPassword()
        )) {
            return MessageResponse.setResponse(
                    false,
                    HttpStatus.UNAUTHORIZED,
                    "Usuario o contrasena incorrectos",
                    null
            );
        }
        List<Integer> idRoles = usuarioRolRepository.listarIdsRolesPorUsuario(usuarioEntity.getIdUsuario());
        List<JwtRole> roles = rolRepository.findAllById(idRoles).stream()
            .map(this::toJwtRole)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(JwtRole::nombre))
            .toList();
        String token = jwtUtil.generateToken(usuarioEntity.getIdUsuario(), usuarioEntity.getUsuario(),
            usuarioEntity.getNombres(), usuarioEntity.getApePaterno(), usuarioEntity.getApeMaterno(), roles, null);
        return MessageResponse.setResponse(true, HttpStatus.OK, "Login exitoso", token);
    }

    @Override
    public ResponseEntity<Object> seleccionarRol(SeleccionarRolRequest request, String token) {
        Integer idUsuario = jwtUtil.extractIdUsuario(token);
        if (idUsuario == null || !jwtUtil.isTokenValid(token, jwtUtil.extractUsuario(token))) {
            return MessageResponse.setResponse(false, HttpStatus.UNAUTHORIZED, "Token invalido", null);
        }
        List<Integer> idsRoles = usuarioRolRepository.listarIdsRolesPorUsuario(idUsuario);
        if (!idsRoles.contains(request.getIdRol())) {
            return MessageResponse.setResponse(false, HttpStatus.FORBIDDEN, "El rol no pertenece al usuario", null);
        }
        RolEntity rolSeleccionado = rolRepository.findById(request.getIdRol()).orElse(null);
        if (rolSeleccionado == null || Boolean.FALSE.equals(rolSeleccionado.getActivo())) {
            return MessageResponse.setResponse(false, HttpStatus.FORBIDDEN, "El rol no esta activo", null);
        }
        List<JwtRole> roles = rolRepository.findAllById(idsRoles).stream()
            .map(this::toJwtRole)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(JwtRole::nombre))
            .toList();
        RolEnum rolEnum = resolveRole(rolSeleccionado);
        if (rolEnum == null) {
            return MessageResponse.setResponse(false, HttpStatus.FORBIDDEN, "El rol no esta configurado", null);
        }
        JwtRole selected = new JwtRole(rolEnum.getId(), rolEnum.getCodigo());
        String nuevoToken = jwtUtil.generateToken(idUsuario, jwtUtil.extractUsuario(token),
            jwtUtil.extractNombres(token), jwtUtil.extractApePaterno(token), jwtUtil.extractApeMaterno(token), roles, selected);
        return MessageResponse.setResponse(true, HttpStatus.OK, "Rol seleccionado", nuevoToken);
    }

    private JwtRole toJwtRole(RolEntity rol) {
        RolEnum rolEnum = resolveRole(rol);
        return rolEnum == null ? null : new JwtRole(rolEnum.getId(), rolEnum.getCodigo());
    }

    private RolEnum resolveRole(RolEntity rol) {
        if (rol == null) {
            return null;
        }
        RolEnum roleByName = RolEnum.fromCodigo(rol.getNombre());
        return roleByName != null ? roleByName : RolEnum.fromId(rol.getIdRol());
    }
}
