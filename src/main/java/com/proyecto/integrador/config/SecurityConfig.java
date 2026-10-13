package com.proyecto.integrador.config;

import com.proyecto.integrador.security.JwtAuthenticationFilter;
import com.proyecto.integrador.security.JwtUtil;
import com.proyecto.integrador.security.RestAuthenticationEntryPoint;
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
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/usuario/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/comunes/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/comunes/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/comunes/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/comunes/**").authenticated()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/incidencias/listar"
                        )
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR_MESA_CONTROL",
                                "SUPERVISOR"
                        )

                        .requestMatchers(
                                HttpMethod.GET,
                                "/incidencias/vencidas"
                        )
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR_MESA_CONTROL",
                                "RESPONSABLE_ATENCION",
                                "SUPERVISOR"
                        )
                        .requestMatchers(
                                HttpMethod.GET,
                                "/incidencias/indicadores"
                        )
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR_MESA_CONTROL",
                                "SUPERVISOR"
                        )
                        .requestMatchers(HttpMethod.GET, "/incidencias/*")
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR_MESA_CONTROL",
                                "RESPONSABLE_ATENCION",
                                "REPORTANTE",
                                "SUPERVISOR"
                        )
                        .requestMatchers(HttpMethod.POST, "/incidencias/registrar")
                        .hasRole("REPORTANTE")
                        .requestMatchers(HttpMethod.PUT, "/incidencias/actualizar")
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR_MESA_CONTROL"
                        )
                        .requestMatchers(HttpMethod.DELETE, "/incidencias/eliminar/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/incidencias/clasificar")
                        .hasRole("OPERADOR_MESA_CONTROL")
                        .requestMatchers(HttpMethod.POST, "/incidencias/asignar-responsable")
                        .hasRole("OPERADOR_MESA_CONTROL")
                        .requestMatchers(HttpMethod.POST, "/incidencias/iniciar-atencion")
                        .hasRole("RESPONSABLE_ATENCION")
                        .requestMatchers(HttpMethod.POST, "/incidencias/resolver")
                        .hasRole("RESPONSABLE_ATENCION")
                        .requestMatchers(HttpMethod.POST, "/incidencias/cerrar")
                        .hasRole("SUPERVISOR")
                        .requestMatchers(HttpMethod.POST, "/incidencias/acciones-correctivas")
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "RESPONSABLE_ATENCION",
                                "SUPERVISOR"
                        )
                        .requestMatchers(HttpMethod.POST, "/incidencias/evidencias")
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR_MESA_CONTROL",
                                "RESPONSABLE_ATENCION",
                                "REPORTANTE",
                                "SUPERVISOR"
                        )
                        .requestMatchers(HttpMethod.POST, "/incidencias/comentarios")
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR_MESA_CONTROL",
                                "RESPONSABLE_ATENCION",
                                "REPORTANTE",
                                "SUPERVISOR"
                        )
                        .requestMatchers(
                                HttpMethod.GET,
                                "/incidencias/*/historial",
                                "/incidencias/*/evidencias",
                                "/incidencias/*/acciones-correctivas",
                                "/incidencias/*/comentarios"
                        )
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "OPERADOR_MESA_CONTROL",
                                "RESPONSABLE_ATENCION",
                                "REPORTANTE",
                                "SUPERVISOR"
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