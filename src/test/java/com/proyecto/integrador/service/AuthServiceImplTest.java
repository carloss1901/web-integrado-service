package com.proyecto.integrador.service;

import com.proyecto.integrador.model.entity.RolEntity;
import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.request.auth.LoginRequest;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.security.JwtUtil;
import com.proyecto.integrador.service.impl.AuthServiceImpl;
import com.proyecto.integrador.util.PasswordHashUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioRolRepository usuarioRolRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private UsuarioEntity usuarioValido() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(1);
        usuario.setUsuario("admin");
        usuario.setNombres("Admin");
        usuario.setApellidos("Sistema");
        usuario.setPassword(PasswordHashUtil.hash("123456"));
        usuario.setEstado(true);
        usuario.setActivo(true);
        return usuario;
    }

    @Test
    void loginConCredencialesValidasDebeRetornarTokenYRoles() {
        UsuarioEntity usuario = usuarioValido();
        RolEntity rol = new RolEntity();
        rol.setIdRol(1);
        rol.setNombre("ADMINISTRADOR");

        when(usuarioRepository.findByUsuarioIgnoreCase("admin")).thenReturn(Optional.of(usuario));
        when(usuarioRolRepository.listarIdsRolesPorUsuario(1)).thenReturn(List.of(1));
        when(rolRepository.findAllById(List.of(1))).thenReturn(List.of(rol));
        when(jwtUtil.generateToken(1, "admin", "Admin", "Sistema", List.of("ADMINISTRADOR"))).thenReturn("token-demo");

        LoginRequest request = new LoginRequest();
        request.setUsuario("admin");
        request.setContrasena("123456");

        ResponseEntity<Object> response = authService.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(true, body.get("success"));
        assertEquals("token-demo", body.get("data"));
        verify(jwtUtil).generateToken(1, "admin", "Admin", "Sistema", List.of("ADMINISTRADOR"));
    }

    @Test
    void loginConUsuarioInexistenteDebeRetornarNoAutorizado() {
        when(usuarioRepository.findByUsuarioIgnoreCase("inexistente")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setUsuario("inexistente");
        request.setContrasena("123456");

        ResponseEntity<Object> response = authService.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(false, body.get("success"));
    }

    @Test
    void loginConUsuarioInactivoDebeRetornarNoAutorizado() {
        UsuarioEntity usuario = usuarioValido();
        usuario.setEstado(false);

        when(usuarioRepository.findByUsuarioIgnoreCase("admin")).thenReturn(Optional.of(usuario));

        LoginRequest request = new LoginRequest();
        request.setUsuario("admin");
        request.setContrasena("123456");

        ResponseEntity<Object> response = authService.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(false, body.get("success"));
        assertEquals("Usuario inactivo", body.get("message"));
    }

    @Test
    void loginConContrasenaIncorrectaDebeRetornarNoAutorizado() {
        UsuarioEntity usuario = usuarioValido();

        when(usuarioRepository.findByUsuarioIgnoreCase("admin")).thenReturn(Optional.of(usuario));

        LoginRequest request = new LoginRequest();
        request.setUsuario("admin");
        request.setContrasena("incorrecta");

        ResponseEntity<Object> response = authService.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertTrue(Boolean.FALSE.equals(body.get("success")));
    }
}