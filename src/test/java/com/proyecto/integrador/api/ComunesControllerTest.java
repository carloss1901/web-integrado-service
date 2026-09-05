package com.proyecto.integrador.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.model.enums.TipoMaestro;
import com.proyecto.integrador.model.request.comunes.MantenerMaestroRequest;
import com.proyecto.integrador.model.response.MaestroResponse;
import com.proyecto.integrador.service.ComunesService;
import com.proyecto.integrador.util.MessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComunesController.class)
class ComunesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ComunesService comunesService;

    @Test
    void listarRolesDebeResponderOk() throws Exception {
        MaestroResponse response = new MaestroResponse();
        response.setMaestroId(1);
        response.setDescripcion("ADMINISTRADOR");

        when(comunesService.listarRoles()).thenReturn(List.of(response));

        mockMvc.perform(get("/comunes/roles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].maestroId").value(1));

        verify(comunesService).listarRoles();
    }

    @Test
    void registrarMaestroDebeResponderOk() throws Exception {
        MantenerMaestroRequest request = new MantenerMaestroRequest();
        request.setDescripcion("BASE_DATOS");

        when(comunesService.registrarMaestro(eq(TipoMaestro.CATEGORIAS), any(MantenerMaestroRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Registro creado correctamente"));

        mockMvc.perform(post("/comunes/categorias/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(comunesService).registrarMaestro(eq(TipoMaestro.CATEGORIAS), any(MantenerMaestroRequest.class));
    }

    @Test
    void actualizarMaestroDebeResponderOk() throws Exception {
        MantenerMaestroRequest request = new MantenerMaestroRequest();
        request.setIdMaestro(1);
        request.setDescripcion("BASE_DATOS");

        when(comunesService.actualizarMaestro(eq(TipoMaestro.CATEGORIAS), any(MantenerMaestroRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Registro actualizado correctamente"));

        mockMvc.perform(put("/comunes/categorias/actualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(comunesService).actualizarMaestro(eq(TipoMaestro.CATEGORIAS), any(MantenerMaestroRequest.class));
    }

    @Test
    void eliminarMaestroDebeResponderOk() throws Exception {
        when(comunesService.eliminarMaestro(TipoMaestro.CATEGORIAS, 1))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Registro eliminado correctamente"));

        mockMvc.perform(delete("/comunes/categorias/eliminar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(comunesService).eliminarMaestro(TipoMaestro.CATEGORIAS, 1);
    }
}
