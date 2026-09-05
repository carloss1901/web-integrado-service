package com.proyecto.integrador.service;

import com.proyecto.integrador.model.enums.TipoMaestro;
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
    ResponseEntity<Object> registrarMaestro(TipoMaestro maestro, MantenerMaestroRequest request);
    ResponseEntity<Object> actualizarMaestro(TipoMaestro maestro, MantenerMaestroRequest request);
    ResponseEntity<Object> eliminarMaestro(TipoMaestro maestro, Integer idMaestro);
}
