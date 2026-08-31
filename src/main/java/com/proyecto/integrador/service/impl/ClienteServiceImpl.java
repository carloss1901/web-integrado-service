package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.config.ConfigProperty;
import com.proyecto.integrador.model.entity.ClienteEntity;
import com.proyecto.integrador.model.projection.ClienteProjection;
import com.proyecto.integrador.model.request.cliente.DesactivarClienteRequest;
import com.proyecto.integrador.model.request.cliente.ListarClienteRequest;
import com.proyecto.integrador.model.request.cliente.RegistrarClienteRequest;
import com.proyecto.integrador.repository.ClienteRepository;
import com.proyecto.integrador.service.ClienteService;
import com.proyecto.integrador.util.CustomPage;
import com.proyecto.integrador.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ConfigProperty message;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<ClienteProjection> listarCliente(ListarClienteRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        return new CustomPage<>(clienteRepository.listarCliente(request, pageable));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registrarCliente(RegistrarClienteRequest request) {
        ClienteEntity entity;
        if (request.getIdCliente() == 0) {
            entity = new ClienteEntity();
            entity.setActivo(Boolean.TRUE);
            entity.setFechaCreacion(Date.valueOf(LocalDate.now()));
        } else {
            entity = clienteRepository.findById(request.getIdCliente()).orElse(null);
            if (entity == null) {
                return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND,
                    message.getMessage("mensaje.notFound"));
            }
            entity.setFechaModificacion(Date.valueOf(LocalDate.now()));
        }

        entity.setNombre(request.getNombre());
        entity.setCodExterno(request.getCodExterno());
        entity.setDireccion(request.getDireccion());
        entity.setCorreoElectronico(request.getCorreo());
        clienteRepository.save(entity);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, message.getMessage("mensaje.api202"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> desactivarCliente(DesactivarClienteRequest request) {
        ClienteEntity entity = clienteRepository.findById(request.getIdCliente()).orElse(null);

        if (entity == null) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.NOT_FOUND,
                message.getMessage("mensaje.notFound"));
        }

        entity.setActivo(request.getActivar());
        entity.setFechaModificacion(Date.valueOf(LocalDate.now()));
        clienteRepository.save(entity);

        return MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, message.getMessage("mensaje.update"));
    }
}
