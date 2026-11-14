package com.proyecto.integrador.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "webIntegradoServiceSecretKeyParaJwtHS256DesarrolloWeb2026");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 28800000L);
    }

    @Test
    void generateTokenDebeIncluirTodaLaInfoDentroDelToken() {
        String token = jwtUtil.generateToken(1, "admin", "Admin", "Sistema", List.of("ADMINISTRADOR"));

        assertEquals("admin", jwtUtil.extractUsuario(token));
        assertEquals(1, jwtUtil.extractIdUsuario(token));
        assertEquals("Admin", jwtUtil.extractNombres(token));
        assertEquals("Sistema", jwtUtil.extractApellidos(token));
        assertEquals(List.of(new JwtRole(5, "ADMINISTRADOR")), jwtUtil.extractRoles(token));
        assertTrue(jwtUtil.isTokenValid(token, "admin"));
    }

    @Test
    void isTokenValidDebeRechazarUsuarioDistinto() {
        String token = jwtUtil.generateToken(1, "admin", "Admin", "Sistema", List.of("ADMINISTRADOR"));

        assertFalse(jwtUtil.isTokenValid(token, "otro"));
    }

    @Test
    void generateTokenDebeGuardarRolesConIdYRolSeleccionado() {
        JwtRole administrador = new JwtRole(1, "ADMINISTRADOR");
        JwtRole supervisor = new JwtRole(5, "SUPERVISOR");
        String token = jwtUtil.generateToken(1, "admin", "Admin", "Sistema",
                List.of(administrador, supervisor), supervisor);

        assertEquals(List.of(administrador, supervisor), jwtUtil.extractRoles(token));
        assertEquals(supervisor, jwtUtil.extractRolSeleccionado(token));
    }
}
