package com.proyecto.integrador.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.model.request.incidencia.ActualizarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.AsignarResponsableRequest;
import com.proyecto.integrador.model.request.incidencia.ClasificarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarIncidenciaRequest;
import com.proyecto.integrador.model.response.IncidenciaResponse;
import com.proyecto.integrador.service.IncidenciaService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidenciaController.class)
class IncidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IncidenciaService incidenciaService;

    @Test
    void listarIncidenciasDebeResponderOk() throws Exception {
        IncidenciaResponse response = new IncidenciaResponse();
        response.setIdIncidencia(1);
        response.setCodigo("INC-20260905000001000");
        response.setTitulo("No enciende el equipo");

        when(incidenciaService.listarIncidencias()).thenReturn(List.of(response));

        mockMvc.perform(get("/incidencias/listar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].titulo").value("No enciende el equipo"));

        verify(incidenciaService).listarIncidencias();
    }

    @Test
    void obtenerIncidenciaDebeUsarPathParam() throws Exception {
        when(incidenciaService.obtenerIncidencia(1))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Proceso exitoso"));

        mockMvc.perform(get("/incidencias/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(incidenciaService).obtenerIncidencia(1);
    }

    @Test
    void registrarIncidenciaDebeResponderOk() throws Exception {
        RegistrarIncidenciaRequest request = new RegistrarIncidenciaRequest();
        request.setIdReportante(1);
        request.setIdCategoria(1);
        request.setIdUbicacion(1);
        request.setTitulo("No enciende el equipo");
        request.setDescripcion("El equipo no muestra imagen");

        when(incidenciaService.registrarIncidencia(any(RegistrarIncidenciaRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia registrada correctamente"));

        mockMvc.perform(post("/incidencias/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(incidenciaService).registrarIncidencia(any(RegistrarIncidenciaRequest.class));
    }

    @Test
    void actualizarIncidenciaDebeResponderOk() throws Exception {
        ActualizarIncidenciaRequest request = new ActualizarIncidenciaRequest();
        request.setIdIncidencia(1);
        request.setIdCategoria(2);
        request.setIdUbicacion(2);
        request.setTitulo("Equipo reparado");
        request.setDescripcion("Actualizacion de detalle");

        when(incidenciaService.actualizarIncidencia(any(ActualizarIncidenciaRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia actualizada correctamente"));

        mockMvc.perform(put("/incidencias/actualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(incidenciaService).actualizarIncidencia(any(ActualizarIncidenciaRequest.class));
    }

    @Test
    void eliminarIncidenciaDebeUsarPathParam() throws Exception {
        when(incidenciaService.eliminarIncidencia(1))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia eliminada correctamente"));

        mockMvc.perform(delete("/incidencias/eliminar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(incidenciaService).eliminarIncidencia(1);
    }

    @Test
    void clasificarIncidenciaDebeResponderOk() throws Exception {
        ClasificarIncidenciaRequest request = new ClasificarIncidenciaRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(2);
        request.setIdSeveridad(1);
        request.setImpacto(2);
        request.setUrgencia(2);
        request.setReincidencia(0);

        when(incidenciaService.clasificarIncidencia(any(ClasificarIncidenciaRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Incidencia clasificada correctamente"));

        mockMvc.perform(post("/incidencias/clasificar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(incidenciaService).clasificarIncidencia(any(ClasificarIncidenciaRequest.class));
    }

    @Test
    void asignarResponsableDebeResponderOk() throws Exception {
        AsignarResponsableRequest request = new AsignarResponsableRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(2);
        request.setIdResponsable(3);

        when(incidenciaService.asignarResponsable(any(AsignarResponsableRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Responsable asignado correctamente"));

        mockMvc.perform(post("/incidencias/asignar-responsable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(incidenciaService).asignarResponsable(any(AsignarResponsableRequest.class));
    }
}