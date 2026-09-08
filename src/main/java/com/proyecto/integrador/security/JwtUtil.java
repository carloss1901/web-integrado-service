package com.proyecto.integrador.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(Integer idUsuario, String usuario, String nombres, String apellidos, List<String> roles) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);
        return Jwts.builder()
            .setSubject(usuario)
            .claim("idUsuario", idUsuario)
            .claim("nombres", nombres)
            .claim("apellidos", apellidos)
            .claim("roles", roles)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractUsuario(String token) {
        return getClaims(token).getSubject();
    }

    public Integer extractIdUsuario(String token) {
        Number value = getClaims(token).get("idUsuario", Number.class);
        return value == null ? null : value.intValue();
    }

    public String extractNombres(String token) {
        return getClaims(token).get("nombres", String.class);
    }

    public String extractApellidos(String token) {
        return getClaims(token).get("apellidos", String.class);
    }

    public List<String> extractRoles(String token) {
        Object roles = getClaims(token).get("roles");
        if (roles instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    public boolean isTokenValid(String token, String usuario) {
        return extractUsuario(token).equals(usuario) && !getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}