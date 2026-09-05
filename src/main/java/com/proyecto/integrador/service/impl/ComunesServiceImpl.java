package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.mapper.GenericMapper;
import com.proyecto.integrador.model.response.MaestroResponse;
import com.proyecto.integrador.repository.ComunesRepository;
import com.proyecto.integrador.service.ComunesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComunesServiceImpl implements ComunesService {

    @Autowired
    private ComunesRepository comunesRepository;

    @Autowired
    private GenericMapper genericMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarRoles() {
        return genericMapper.toResponseList(comunesRepository.listarRoles(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarCategorias() {
        return genericMapper.toResponseList(comunesRepository.listarCategorias(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarUbicaciones() {
        return genericMapper.toResponseList(comunesRepository.listarUbicaciones(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarSeveridades() {
        return genericMapper.toResponseList(comunesRepository.listarSeveridades(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarPrioridades() {
        return genericMapper.toResponseList(comunesRepository.listarPrioridades(), MaestroResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listarEstadosIncidencia() {
        return genericMapper.toResponseList(comunesRepository.listarEstadosIncidencia(), MaestroResponse.class);
    }
}
