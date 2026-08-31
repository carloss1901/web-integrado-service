package com.proyecto.integrador.service;

import com.proyecto.integrador.model.projection.ClienteProjection;
import com.proyecto.integrador.model.request.cliente.DesactivarClienteRequest;
import com.proyecto.integrador.model.request.cliente.ListarClienteRequest;
import com.proyecto.integrador.model.request.cliente.RegistrarClienteRequest;
import com.proyecto.integrador.util.CustomPage;
import org.springframework.http.ResponseEntity;

public interface ClienteService {
    CustomPage<ClienteProjection> listarCliente(ListarClienteRequest request);
    ResponseEntity<Object> registrarCliente(RegistrarClienteRequest request);
    ResponseEntity<Object> desactivarCliente(DesactivarClienteRequest request);
}
