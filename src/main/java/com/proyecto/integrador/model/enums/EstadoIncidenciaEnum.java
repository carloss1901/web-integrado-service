package com.proyecto.integrador.model.enums;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Matriz única del ciclo de vida de una incidencia.
 *
 * <p>En espera representa un bloqueo temporal y solo puede volver a En atención.
 * Cancelada y Cerrada son estados terminales. Reabierta representa una resolución
 * observada por el supervisor y debe volver a En atención antes de poder resolverse.</p>
 */
public enum EstadoIncidenciaEnum {
    REGISTRADA(1),
    CLASIFICADA(2),
    ASIGNADA(3),
    EN_ATENCION(4),
    EN_ESPERA(5),
    RESUELTA(6),
    CERRADA(7),
    CANCELADA(8),
    REABIERTA(9);

    private static final Map<EstadoIncidenciaEnum, Set<EstadoIncidenciaEnum>> TRANSICIONES =
            new EnumMap<>(EstadoIncidenciaEnum.class);

    static {
        TRANSICIONES.put(REGISTRADA, EnumSet.of(CLASIFICADA, CANCELADA));
        TRANSICIONES.put(CLASIFICADA, EnumSet.of(ASIGNADA, CANCELADA));
        TRANSICIONES.put(ASIGNADA, EnumSet.of(EN_ATENCION, CANCELADA));
        TRANSICIONES.put(EN_ATENCION, EnumSet.of(EN_ESPERA, RESUELTA));
        TRANSICIONES.put(EN_ESPERA, EnumSet.of(EN_ATENCION, CANCELADA));
        TRANSICIONES.put(RESUELTA, EnumSet.of(CERRADA, REABIERTA));
        TRANSICIONES.put(REABIERTA, EnumSet.of(EN_ATENCION));
        TRANSICIONES.put(CERRADA, EnumSet.noneOf(EstadoIncidenciaEnum.class));
        TRANSICIONES.put(CANCELADA, EnumSet.noneOf(EstadoIncidenciaEnum.class));
    }

    private final int id;

    EstadoIncidenciaEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean permiteTransicionA(EstadoIncidenciaEnum destino) {
        return TRANSICIONES.get(this).contains(destino);
    }

    public static EstadoIncidenciaEnum fromId(Integer id) {
        if (id == null) {
            return null;
        }
        for (EstadoIncidenciaEnum estado : values()) {
            if (estado.id == id) {
                return estado;
            }
        }
        return null;
    }
}
