package com.proyecto.integrador.model.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EstadoIncidenciaEnumTest {

    @Test
    void matrizDebePermitirSoloLosSaltosDefinidos() {
        Map<EstadoIncidenciaEnum, Set<EstadoIncidenciaEnum>> esperadas =
                new EnumMap<>(EstadoIncidenciaEnum.class);
        esperadas.put(EstadoIncidenciaEnum.REGISTRADA,
                EnumSet.of(EstadoIncidenciaEnum.CLASIFICADA, EstadoIncidenciaEnum.CANCELADA));
        esperadas.put(EstadoIncidenciaEnum.CLASIFICADA,
                EnumSet.of(EstadoIncidenciaEnum.ASIGNADA, EstadoIncidenciaEnum.CANCELADA));
        esperadas.put(EstadoIncidenciaEnum.ASIGNADA,
                EnumSet.of(EstadoIncidenciaEnum.EN_ATENCION, EstadoIncidenciaEnum.CANCELADA));
        esperadas.put(EstadoIncidenciaEnum.EN_ATENCION,
                EnumSet.of(EstadoIncidenciaEnum.EN_ESPERA, EstadoIncidenciaEnum.RESUELTA));
        esperadas.put(EstadoIncidenciaEnum.EN_ESPERA,
                EnumSet.of(EstadoIncidenciaEnum.EN_ATENCION, EstadoIncidenciaEnum.CANCELADA));
        esperadas.put(EstadoIncidenciaEnum.RESUELTA,
                EnumSet.of(EstadoIncidenciaEnum.CERRADA, EstadoIncidenciaEnum.REABIERTA));
        esperadas.put(EstadoIncidenciaEnum.REABIERTA,
                EnumSet.of(EstadoIncidenciaEnum.EN_ATENCION));
        esperadas.put(EstadoIncidenciaEnum.CERRADA,
                EnumSet.noneOf(EstadoIncidenciaEnum.class));
        esperadas.put(EstadoIncidenciaEnum.CANCELADA,
                EnumSet.noneOf(EstadoIncidenciaEnum.class));

        for (EstadoIncidenciaEnum origen : EstadoIncidenciaEnum.values()) {
            for (EstadoIncidenciaEnum destino : EstadoIncidenciaEnum.values()) {
                assertEquals(
                        esperadas.get(origen).contains(destino),
                        origen.permiteTransicionA(destino),
                        () -> origen + " -> " + destino
                );
            }
        }
    }
}
