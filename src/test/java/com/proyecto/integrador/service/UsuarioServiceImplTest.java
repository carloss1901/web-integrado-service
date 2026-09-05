package com.proyecto.integrador.service;

import com.proyecto.integrador.model.entity.UsuarioEntity;
import com.proyecto.integrador.model.request.usuario.ActualizarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.repository.RolRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.repository.UsuarioRolRepository;
import com.proyecto.integrador.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioRolRepository usuarioRolRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void registrarUsuarioDebeCrearUsuarioYRoles() {
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest();
        request.setUsuario("jperez");
        request.setNombres("Juan");
        request.setApellidos("Perez");
        request.setCorreo("jperez@demo.com");
        request.setPassword("123456");
        request.setIdsRoles(List.of(4));

        when(usuarioRepository.existsByUsuarioIgnoreCase("jperez")).thenReturn(false);
        when(usuarioRepository.existsByCorreoIgnoreCase("jperez@demo.com")).thenReturn(false);
        when(rolRepository.existsById(4)).thenReturn(true);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(invocation -> {
            UsuarioEntity usuario = invocation.getArgument(0);
            usuario.setIdUsuario(10);
            return usuario;
        });

        ResponseEntity<Object> response = usuarioService.registrarUsuario(request);

        assertEquals(200, response.getStatusCode().value());
        verify(usuarioRepository).save(any(UsuarioEntity.class));
        verify(usuarioRolRepository).save(any());
    }

    @Test
    void actualizarUsuarioDebeActualizarDatosYRoles() {
        ActualizarUsuarioRequest request = new ActualizarUsuarioRequest();
        request.setIdUsuario(1);
        request.setNombres("Juan");
        request.setApellidos("Perez");
        request.setCorreo("jperez@demo.com");
        request.setIdsRoles(List.of(2, 4));

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(1);
        usuario.setUsuario("jperez");
        usuario.setCorreo("old@demo.com");
        usuario.setActivo(Boolean.TRUE);

        when(usuarioRepository.countByCorreoAndIdUsuarioNot("jperez@demo.com", 1)).thenReturn(0);
        when(rolRepository.existsById(2)).thenReturn(true);
        when(rolRepository.existsById(4)).thenReturn(true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        ResponseEntity<Object> response = usuarioService.actualizarUsuario(request);

        assertEquals(200, response.getStatusCode().value());
        verify(usuarioRepository).save(usuario);
        verify(usuarioRolRepository).deleteByIdUsuario(1);
        verify(usuarioRolRepository, org.mockito.Mockito.times(2)).save(any());
    }
}
