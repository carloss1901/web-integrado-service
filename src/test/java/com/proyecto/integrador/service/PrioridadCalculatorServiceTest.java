package com.proyecto.integrador.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrioridadCalculatorServiceTest {

    private final PrioridadCalculatorService calculator = new PrioridadCalculatorService(
            new BigDecimal("0.40"), new BigDecimal("0.40"), new BigDecimal("0.20"));

    @Test
    void calculaCasosEsperados() {
        assertResultado(1, 1, 1, "1.00", "BAJA");
        assertResultado(2, 2, 2, "2.00", "MEDIA");
        assertResultado(3, 3, 3, "3.00", "ALTA");
        assertResultado(4, 4, 4, "4.00", "CRITICA");
        assertResultado(3, 4, 2, "3.20", "ALTA");
    }

    @Test
    void respetaLimitesDeClasificacion() {
        assertEquals("MEDIA", calculator.calcular(2, 2, 1).prioridad()); // 1.80
        assertEquals("ALTA", calculator.calcular(3, 2, 1).prioridad()); // 2.20
        assertEquals("CRITICA", calculator.calcular(4, 3, 3).prioridad()); // 3.40
    }

    @Test
    void rechazaValoresFueraDeEscala() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calcular(0, 2, 1));
        assertThrows(IllegalArgumentException.class, () -> calculator.calcular(2, 5, 1));
        assertThrows(IllegalArgumentException.class, () -> calculator.calcular(2, 2, 0));
    }

    private void assertResultado(int impacto, int urgencia, int reincidencia,
                                 String puntuacion, String prioridad) {
        PrioridadCalculatorService.Resultado resultado = calculator.calcular(impacto, urgencia, reincidencia);
        assertEquals(new BigDecimal(puntuacion), resultado.puntuacion());
        assertEquals(prioridad, resultado.prioridad());
    }
}
