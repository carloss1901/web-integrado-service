package com.proyecto.integrador.security;

import com.proyecto.integrador.api.IncidenciaController;
import com.proyecto.integrador.config.SecurityConfig;
import com.proyecto.integrador.service.IncidenciaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import com.proyecto.integrador.config.Cors;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import org.springframework.http.HttpHeaders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@WebMvcTest(
        controllers = IncidenciaController.class,
        excludeAutoConfiguration =
                UserDetailsServiceAutoConfiguration.class
)

@Import({
        SecurityConfig.class,
        Cors.class,
        JwtUtil.class,
        RestAuthenticationEntryPoint.class
})

@TestPropertySource(properties = {
        "jwt.secret=webIntegradoServiceSecretKeyParaJwtHS256DesarrolloWeb2026",
        "jwt.expiration=28800000"
})

class IncidenciaSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private IncidenciaService incidenciaService;

    @Test
    void listarIncidenciasSinTokenDebeResponder401() throws Exception {
        mockMvc.perform(get("/incidencias/listar"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listarIncidenciasConReportanteDebeResponder403() throws Exception {
        String token = jwtUtil.generateToken(
                4,
                "reportante",
                "Usuario",
                "Reportante",
                List.of("REPORTANTE")
        );

        mockMvc.perform(get("/incidencias/listar")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void listarIncidenciasConAdministradorDebeResponder200() throws Exception {
        String token = jwtUtil.generateToken(
                1,
                "admin",
                "Administrador",
                "Sistema",
                List.of("ADMINISTRADOR")
        );

        when(incidenciaService.listarIncidencias())
                .thenReturn(List.of());

        mockMvc.perform(get("/incidencias/listar")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void peticionCorsDesdeReactDebeResponderCorrectamente() throws Exception {
        mockMvc.perform(options("/incidencias/listar")
                        .header(
                                HttpHeaders.ORIGIN,
                                "http://localhost:5173"
                        )
                        .header(
                                HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD,
                                "GET"
                        ))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "http://localhost:5173"
                ))
                .andExpect(header().string(
                        HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                        "true"
                ));
    }

    @Test
    void indicadoresConReportanteDebeResponder403() throws Exception {
        String token = jwtUtil.generateToken(
                4,
                "reportante",
                "Usuario",
                "Reportante",
                List.of("REPORTANTE")
        );

        mockMvc.perform(get("/incidencias/indicadores")
                        .header(
                                "Authorization",
                                "Bearer " + token
                        ))
                .andExpect(status().isForbidden());
    }

    @Test
    void indicadoresConAdministradorDebeResponder200() throws Exception {
        String token = jwtUtil.generateToken(
                1,
                "admin",
                "Administrador",
                "Sistema",
                List.of("ADMINISTRADOR")
        );

        when(incidenciaService.obtenerIndicadores())
                .thenReturn(null);

        mockMvc.perform(get("/incidencias/indicadores")
                        .header(
                                "Authorization",
                                "Bearer " + token
                        ))
                .andExpect(status().isOk());
    }
}