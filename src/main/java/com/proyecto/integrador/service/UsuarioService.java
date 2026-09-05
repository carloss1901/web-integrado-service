package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.usuario.ActualizarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.ListarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.model.projection.UsuarioProjection;
import com.proyecto.integrador.util.CustomPage;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    CustomPage<UsuarioProjection> listarUsuario(ListarUsuarioRequest request);
    ResponseEntity<Object> obtenerUsuario(Integer idUsuario);
    ResponseEntity<Object> registrarUsuario(RegistrarUsuarioRequest request);
    ResponseEntity<Object> actualizarUsuario(ActualizarUsuarioRequest request);
    ResponseEntity<Object> eliminarUsuario(Integer idUsuario);
}
