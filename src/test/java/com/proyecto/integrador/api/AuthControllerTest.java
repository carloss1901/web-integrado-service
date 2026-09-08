package com.proyecto.integrador.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.model.request.auth.LoginRequest;
import com.proyecto.integrador.service.AuthService;
import com.proyecto.integrador.util.MessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = {
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void loginConCredencialesValidasDebeResponderOk() throws Exception {
        when(authService.login(any(LoginRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.TRUE, HttpStatus.OK, "Login exitoso", "token-demo"));

        LoginRequest request = new LoginRequest();
        request.setUsuario("admin");
        request.setContrasena("123456");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value("token-demo"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void loginConCredencialesInvalidasDebeResponderNoAutorizado() throws Exception {
        when(authService.login(any(LoginRequest.class)))
            .thenReturn(MessageResponse.setResponse(Boolean.FALSE, HttpStatus.UNAUTHORIZED, "Usuario o contrasena incorrectos"));

        LoginRequest request = new LoginRequest();
        request.setUsuario("admin");
        request.setContrasena("mal");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false));

        verify(authService).login(any(LoginRequest.class));
    }
}