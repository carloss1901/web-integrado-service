package com.proyecto.integrador.service;

import com.proyecto.integrador.model.request.usuario.ActualizarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.ListarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.model.response.UsuarioListadoResponse;
import com.proyecto.integrador.model.response.ResponsableResponse;
import com.proyecto.integrador.util.CustomPage;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface UsuarioService {
    CustomPage<UsuarioListadoResponse> listarUsuario(ListarUsuarioRequest request);
    List<ResponsableResponse> listarResponsablesActivos();
    ResponseEntity<Object> obtenerUsuario(Integer idUsuario);
    ResponseEntity<Object> registrarUsuario(RegistrarUsuarioRequest request);
    ResponseEntity<Object> actualizarUsuario(ActualizarUsuarioRequest request);
    ResponseEntity<Object> eliminarUsuario(Integer idUsuario);
    ResponseEntity<Object> activarUsuario(Integer idUsuario);
}
