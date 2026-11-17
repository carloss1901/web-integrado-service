package com.proyecto.integrador.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardIncidenciasResponse {
    private Long totalPeriodo;
    private Long dentroSla;
    private Long incumplidasSla;
    private List<IndicadorAgrupadoResponse> porZona;
    private List<IndicadorAgrupadoResponse> porCategoria;
    private List<IndicadorAgrupadoResponse> porSeveridad;
    private List<CargaResponsableResponse> cargaResponsables;
}
