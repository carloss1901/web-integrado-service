package com.proyecto.integrador.service;

import com.proyecto.integrador.model.response.MaestroResponse;

import java.util.List;

public interface ComunesService {
    List<MaestroResponse> listarRoles();
    List<MaestroResponse> listarCategorias();
    List<MaestroResponse> listarUbicaciones();
    List<MaestroResponse> listarSeveridades();
    List<MaestroResponse> listarPrioridades();
    List<MaestroResponse> listarEstadosIncidencia();
}
