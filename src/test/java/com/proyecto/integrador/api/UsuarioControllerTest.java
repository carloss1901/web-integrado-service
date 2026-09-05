package com.proyecto.integrador.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.model.request.usuario.ActualizarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void registrarUsuarioDebeResponderOk() throws Exception {
        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest();
        request.setUsuario("jperez");
        request.setNombres("Juan");
        request.setApellidos("Perez");
        request.setCorreo("jperez@demo.com");
        request.setPassword("123456");
        request.setIdsRoles(List.of(4));

        when(usuarioService.registrarUsuario(any(RegistrarUsuarioRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Usuario registrado correctamente"));

        mockMvc.perform(post("/usuario/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Usuario registrado correctamente"));

        verify(usuarioService).registrarUsuario(any(RegistrarUsuarioRequest.class));
    }

    @Test
    void actualizarUsuarioDebeResponderOk() throws Exception {
        ActualizarUsuarioRequest request = new ActualizarUsuarioRequest();
        request.setIdUsuario(1);
        request.setNombres("Juan");
        request.setApellidos("Perez");
        request.setCorreo("jperez@demo.com");
        request.setIdsRoles(List.of(2, 4));

        when(usuarioService.actualizarUsuario(any(ActualizarUsuarioRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Usuario actualizado correctamente"));

        mockMvc.perform(put("/usuario/actualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("Usuario actualizado correctamente"));

        verify(usuarioService).actualizarUsuario(any(ActualizarUsuarioRequest.class));
    }

    @Test
    void obtenerUsuarioDebeUsarPathParam() throws Exception {
        when(usuarioService.obtenerUsuario(1))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Proceso exitoso"));

        mockMvc.perform(get("/usuario/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(usuarioService).obtenerUsuario(1);
    }

    @Test
    void eliminarUsuarioDebeUsarPathParam() throws Exception {
        when(usuarioService.eliminarUsuario(1))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Usuario eliminado correctamente"));

        mockMvc.perform(delete("/usuario/eliminar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(usuarioService).eliminarUsuario(eq(1));
    }
}
