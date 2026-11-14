package com.proyecto.integrador.security;

import com.proyecto.integrador.model.enums.RolEnum;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7);
                String usuario = jwtUtil.extractUsuario(token);
                if (StringUtils.hasText(usuario) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.isTokenValid(token, usuario)) {
                        Integer idUsuario = jwtUtil.extractIdUsuario(token);
                        request.setAttribute("idUsuarioAutenticado", idUsuario);
                        JwtRole selectedRole = jwtUtil.extractRolSeleccionado(token);
                        List<SimpleGrantedAuthority> authorities = selectedRole == null
                            ? List.of()
                            : List.of(selectedRole)
                            .stream()
                            .map(this::resolveRole)
                            .filter(Objects::nonNull)
                            .map(RolEnum::getCodigo)
                            .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol))
                            .toList();
                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (JwtException | IllegalArgumentException ex) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    private RolEnum resolveRole(JwtRole role) {
        if (role == null) {
            return null;
        }
        RolEnum roleByName = RolEnum.fromCodigo(role.nombre());
        return roleByName != null ? roleByName : RolEnum.fromId(role.id());
    }
}
