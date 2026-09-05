package com.proyecto.integrador.service;

import com.proyecto.integrador.model.entity.CategoriaEntity;
import com.proyecto.integrador.model.request.comunes.MantenerMaestroRequest;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.service.impl.ComunesServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComunesServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ComunesServiceImpl comunesService;

    @Test
    void registrarCategoriaDebeResponderOk() {
        MantenerMaestroRequest request = new MantenerMaestroRequest();
        request.setDescripcion("BASE_DATOS");
        request.setDetalle("Incidencias de base de datos");

        ResponseEntity<Object> response = comunesService.registrarMaestro("categorias", request);

        assertEquals(200, response.getStatusCode().value());
        verify(categoriaRepository).save(any(CategoriaEntity.class));
    }

    @Test
    void eliminarCategoriaDebeSerLogico() {
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setIdCategoria(1);
        categoria.setActivo(Boolean.TRUE);

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        ResponseEntity<Object> response = comunesService.eliminarMaestro("categorias", 1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(Boolean.FALSE, categoria.getActivo());
        verify(categoriaRepository).save(categoria);
    }
}
