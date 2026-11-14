package com.proyecto.integrador.config;

import com.proyecto.integrador.security.JwtAuthenticationFilter;
import com.proyecto.integrador.security.JwtUtil;
import com.proyecto.integrador.security.RestAuthenticationEntryPoint;
import com.proyecto.integrador.model.enums.RolEnum;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfig(JwtUtil jwtUtil, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(restAuthenticationEntryPoint))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/seleccionar-rol").authenticated()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Temporalmente cualquier usuario autenticado puede gestionar usuarios.
                        .requestMatchers("/usuario/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/comunes/**")
                        .hasRole(RolEnum.ADMINISTRADOR.getCodigo())
                        .requestMatchers(HttpMethod.PUT, "/comunes/**")
                        .hasRole(RolEnum.ADMINISTRADOR.getCodigo())
                        .requestMatchers(HttpMethod.DELETE, "/comunes/**")
                        .hasRole(RolEnum.ADMINISTRADOR.getCodigo())
                        .requestMatchers(HttpMethod.GET, "/comunes/**").authenticated()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/incidencias/listar"
                        )
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.OPERADOR_MESA_CONTROL.getCodigo(),
                                RolEnum.SUPERVISOR.getCodigo()
                        )

                        .requestMatchers(
                                HttpMethod.GET,
                                "/incidencias/vencidas"
                        )
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.OPERADOR_MESA_CONTROL.getCodigo(),
                                RolEnum.RESPONSABLE_ATENCION.getCodigo(),
                                RolEnum.SUPERVISOR.getCodigo()
                        )
                        .requestMatchers(
                                HttpMethod.GET,
                                "/incidencias/indicadores"
                        )
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.OPERADOR_MESA_CONTROL.getCodigo(),
                                RolEnum.SUPERVISOR.getCodigo()
                        )
                        .requestMatchers(HttpMethod.GET, "/incidencias/*")
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.OPERADOR_MESA_CONTROL.getCodigo(),
                                RolEnum.RESPONSABLE_ATENCION.getCodigo(),
                                RolEnum.REPORTANTE.getCodigo(),
                                RolEnum.SUPERVISOR.getCodigo()
                        )
                        .requestMatchers(HttpMethod.POST, "/incidencias/registrar")
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.REPORTANTE.getCodigo()
                        )
                        .requestMatchers(HttpMethod.PUT, "/incidencias/actualizar")
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.OPERADOR_MESA_CONTROL.getCodigo()
                        )
                        .requestMatchers(HttpMethod.DELETE, "/incidencias/eliminar/**")
                        .hasRole(RolEnum.ADMINISTRADOR.getCodigo())
                        .requestMatchers(HttpMethod.POST, "/incidencias/clasificar")
                        .hasRole(RolEnum.OPERADOR_MESA_CONTROL.getCodigo())
                        .requestMatchers(HttpMethod.POST, "/incidencias/asignar-responsable")
                        .hasRole(RolEnum.OPERADOR_MESA_CONTROL.getCodigo())
                        .requestMatchers(HttpMethod.POST, "/incidencias/iniciar-atencion")
                        .hasRole(RolEnum.RESPONSABLE_ATENCION.getCodigo())
                        .requestMatchers(HttpMethod.POST, "/incidencias/resolver")
                        .hasRole(RolEnum.RESPONSABLE_ATENCION.getCodigo())
                        .requestMatchers(HttpMethod.POST, "/incidencias/cerrar")
                        .hasRole(RolEnum.SUPERVISOR.getCodigo())
                        .requestMatchers(HttpMethod.POST, "/incidencias/acciones-correctivas")
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.RESPONSABLE_ATENCION.getCodigo(),
                                RolEnum.SUPERVISOR.getCodigo()
                        )
                        .requestMatchers(HttpMethod.POST, "/incidencias/evidencias")
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.OPERADOR_MESA_CONTROL.getCodigo(),
                                RolEnum.RESPONSABLE_ATENCION.getCodigo(),
                                RolEnum.REPORTANTE.getCodigo(),
                                RolEnum.SUPERVISOR.getCodigo()
                        )
                        .requestMatchers(HttpMethod.POST, "/incidencias/comentarios")
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.OPERADOR_MESA_CONTROL.getCodigo(),
                                RolEnum.RESPONSABLE_ATENCION.getCodigo(),
                                RolEnum.REPORTANTE.getCodigo(),
                                RolEnum.SUPERVISOR.getCodigo()
                        )
                        .requestMatchers(
                                HttpMethod.GET,
                                "/incidencias/*/historial",
                                "/incidencias/*/evidencias",
                                "/incidencias/*/acciones-correctivas",
                                "/incidencias/*/comentarios"
                        )
                        .hasAnyRole(
                                RolEnum.ADMINISTRADOR.getCodigo(),
                                RolEnum.OPERADOR_MESA_CONTROL.getCodigo(),
                                RolEnum.RESPONSABLE_ATENCION.getCodigo(),
                                RolEnum.REPORTANTE.getCodigo(),
                                RolEnum.SUPERVISOR.getCodigo()
                        )
                        .requestMatchers("/incidencias/**").authenticated()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().denyAll())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
