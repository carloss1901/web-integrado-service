package com.proyecto.integrador.security;

import com.proyecto.integrador.api.UsuarioController;
import com.proyecto.integrador.config.Cors;
import com.proyecto.integrador.config.SecurityConfig;
import com.proyecto.integrador.service.UsuarioService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UsuarioController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
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
class UsuarioSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void usuarioSinTokenDebeResponder401() throws Exception {
        mockMvc.perform(get("/usuario/listar-responsables"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void reportanteNoPuedeGestionarUsuarios() throws Exception {
        String token = jwtUtil.generateToken(
                4, "reportante", "Usuario", "Reportante", null, List.of("REPORTANTE")
        );

        mockMvc.perform(get("/usuario/listar-responsables")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void administradorPuedeGestionarUsuarios() throws Exception {
        String token = jwtUtil.generateToken(
                1, "admin", "Administrador", "Sistema", null, List.of("ADMINISTRADOR")
        );
        when(usuarioService.listarResponsablesActivos()).thenReturn(List.of());

        mockMvc.perform(get("/usuario/listar-responsables")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
