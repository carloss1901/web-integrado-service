package com.proyecto.integrador.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.model.request.incidencia.ActualizarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.AsignarResponsableRequest;
import com.proyecto.integrador.model.request.incidencia.ClasificarIncidenciaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarAccionCorrectivaRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarComentarioRequest;
import com.proyecto.integrador.model.request.incidencia.RegistrarIncidenciaRequest;
import com.proyecto.integrador.model.response.IncidenciaAccionCorrectivaResponse;
import com.proyecto.integrador.model.response.IncidenciaComentarioResponse;
import com.proyecto.integrador.model.response.IncidenciaEvidenciaResponse;
import com.proyecto.integrador.model.response.IncidenciaHistorialResponse;
import com.proyecto.integrador.model.response.IncidenciaResponse;
import com.proyecto.integrador.service.IncidenciaService;
import com.proyecto.integrador.util.CustomPage;
import com.proyecto.integrador.util.MessageResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = IncidenciaController.class, excludeAutoConfiguration = {
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
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
    void listarHistorialDebeResponderOk() throws Exception {
        IncidenciaHistorialResponse response = new IncidenciaHistorialResponse();
        response.setIdHistorial(1);
        response.setTipoEvento("REGISTRO_INCIDENCIA");

        CustomPage<IncidenciaHistorialResponse> page = new CustomPage<>(
            new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));
        when(incidenciaService.listarHistorial(1, 1, 10)).thenReturn(page);

        mockMvc.perform(get("/incidencias/1/historial"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageable.pageNumber").value(1))
            .andExpect(jsonPath("$.data[0].tipoEvento").value("REGISTRO_INCIDENCIA"));

        verify(incidenciaService).listarHistorial(1, 1, 10);
    }

    @Test
    void listarEvidenciasDebeResponderOk() throws Exception {
        IncidenciaEvidenciaResponse response = new IncidenciaEvidenciaResponse();
        response.setIdEvidencia(1);
        response.setNombreArchivo("captura.png");

        CustomPage<IncidenciaEvidenciaResponse> page = new CustomPage<>(
            new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));
        when(incidenciaService.listarEvidencias(1, 1, 10)).thenReturn(page);

        mockMvc.perform(get("/incidencias/1/evidencias"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageable.pageNumber").value(1))
            .andExpect(jsonPath("$.data[0].nombreArchivo").value("captura.png"));

        verify(incidenciaService).listarEvidencias(1, 1, 10);
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

    @Test
    void registrarAccionCorrectivaDebeResponderOk() throws Exception {
        RegistrarAccionCorrectivaRequest request = new RegistrarAccionCorrectivaRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(3);
        request.setDescripcion("Se reemplazo el disco danado");

        when(incidenciaService.registrarAccionCorrectiva(any(RegistrarAccionCorrectivaRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK,
                "Accion correctiva registrada correctamente"));

        mockMvc.perform(post("/incidencias/acciones-correctivas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(incidenciaService).registrarAccionCorrectiva(any(RegistrarAccionCorrectivaRequest.class));
    }

    @Test
    void registrarComentarioDebeResponderOk() throws Exception {
        RegistrarComentarioRequest request = new RegistrarComentarioRequest();
        request.setIdIncidencia(1);
        request.setIdUsuario(3);
        request.setComentario("Se coordino el reemplazo con el area de TI");

        when(incidenciaService.registrarComentario(any(RegistrarComentarioRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Comentario registrado correctamente"));

        mockMvc.perform(post("/incidencias/comentarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(incidenciaService).registrarComentario(any(RegistrarComentarioRequest.class));
    }

    @Test
    void listarAccionesCorrectivasDebeResponderOk() throws Exception {
        IncidenciaAccionCorrectivaResponse response = new IncidenciaAccionCorrectivaResponse();
        response.setIdAccionCorrectiva(1);
        response.setDescripcion("Se reemplazo el disco danado");

        CustomPage<IncidenciaAccionCorrectivaResponse> page = new CustomPage<>(
            new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));
        when(incidenciaService.listarAccionesCorrectivas(1, 1, 10)).thenReturn(page);

        mockMvc.perform(get("/incidencias/1/acciones-correctivas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageable.pageNumber").value(1))
            .andExpect(jsonPath("$.data[0].descripcion").value("Se reemplazo el disco danado"));

        verify(incidenciaService).listarAccionesCorrectivas(1, 1, 10);
    }

    @Test
    void listarIncidenciasVencidasDebeResponderOk() throws Exception {
        IncidenciaResponse response = new IncidenciaResponse();
        response.setIdIncidencia(1);
        response.setTitulo("Sin atender dentro del SLA");

        CustomPage<IncidenciaResponse> page = new CustomPage<>(
            new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));
        when(incidenciaService.listarIncidenciasVencidas(1, 10)).thenReturn(page);

        mockMvc.perform(get("/incidencias/vencidas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageable.pageNumber").value(1))
            .andExpect(jsonPath("$.data[0].titulo").value("Sin atender dentro del SLA"));

        verify(incidenciaService).listarIncidenciasVencidas(1, 10);
    }

    @Test
    void listarComentariosDebeResponderOk() throws Exception {
        IncidenciaComentarioResponse response = new IncidenciaComentarioResponse();
        response.setIdComentario(1);
        response.setComentario("Se coordino el reemplazo con el area de TI");

        CustomPage<IncidenciaComentarioResponse> page = new CustomPage<>(
            new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));
        when(incidenciaService.listarComentarios(1, 1, 10)).thenReturn(page);

        mockMvc.perform(get("/incidencias/1/comentarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageable.pageNumber").value(1))
            .andExpect(jsonPath("$.data[0].comentario").value("Se coordino el reemplazo con el area de TI"));

        verify(incidenciaService).listarComentarios(1, 1, 10);
    }
}