package com.proyecto.integrador.service;

import com.proyecto.integrador.model.response.MaestroResponse;
import com.proyecto.integrador.model.request.comunes.MantenerMaestroRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ComunesService {
    List<MaestroResponse> listarRoles();
    List<MaestroResponse> listarCategorias();
    List<MaestroResponse> listarUbicaciones();
    List<MaestroResponse> listarSeveridades();
    List<MaestroResponse> listarPrioridades();
    List<MaestroResponse> listarEstadosIncidencia();
    ResponseEntity<Object> registrarMaestro(String maestro, MantenerMaestroRequest request);
    ResponseEntity<Object> actualizarMaestro(String maestro, MantenerMaestroRequest request);
    ResponseEntity<Object> eliminarMaestro(String maestro, Integer idMaestro);
}
