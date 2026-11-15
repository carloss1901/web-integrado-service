package com.proyecto.integrador.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PrioridadCalculatorService {

    private final BigDecimal pesoImpacto;
    private final BigDecimal pesoUrgencia;
    private final BigDecimal pesoReincidencia;

    public PrioridadCalculatorService(
            @Value("${app.incidencia.prioridad.peso-impacto:0.40}") BigDecimal pesoImpacto,
            @Value("${app.incidencia.prioridad.peso-urgencia:0.40}") BigDecimal pesoUrgencia,
            @Value("${app.incidencia.prioridad.peso-reincidencia:0.20}") BigDecimal pesoReincidencia) {
        this.pesoImpacto = pesoImpacto;
        this.pesoUrgencia = pesoUrgencia;
        this.pesoReincidencia = pesoReincidencia;
    }

    public Resultado calcular(Integer impacto, Integer urgencia, Integer reincidencia) {
        validar(impacto, "impacto");
        validar(urgencia, "urgencia");
        validar(reincidencia, "reincidencia");

        BigDecimal puntuacion = BigDecimal.valueOf(impacto).multiply(pesoImpacto)
                .add(BigDecimal.valueOf(urgencia).multiply(pesoUrgencia))
                .add(BigDecimal.valueOf(reincidencia).multiply(pesoReincidencia))
                .setScale(2, RoundingMode.HALF_UP);

        return new Resultado(puntuacion, prioridad(puntuacion));
    }

    private void validar(Integer valor, String nombre) {
        if (valor == null || valor < 1 || valor > 4) {
            throw new IllegalArgumentException("El " + nombre + " debe estar entre 1 y 4");
        }
    }

    private String prioridad(BigDecimal puntuacion) {
        if (puntuacion.compareTo(new BigDecimal("1.75")) < 0) return "BAJA";
        if (puntuacion.compareTo(new BigDecimal("2.50")) < 0) return "MEDIA";
        if (puntuacion.compareTo(new BigDecimal("3.25")) < 0) return "ALTA";
        return "CRITICA";
    }

    public record Resultado(BigDecimal puntuacion, String prioridad) { }
}
