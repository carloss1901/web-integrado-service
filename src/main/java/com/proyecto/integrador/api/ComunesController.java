package com.proyecto.integrador.api;

import com.proyecto.integrador.model.response.MaestroResponse;
import com.proyecto.integrador.service.ComunesService;
import com.proyecto.integrador.util.Constantes;
import com.proyecto.integrador.util.ErrorGenerico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Comunes Controller")
@Validated
@RestController
@RequestMapping("/comunes")
public class ComunesController {

    @Autowired
    private ComunesService comunesService;

    @GetMapping(value = "roles", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar roles", description = "Listar roles", responses = {
        @ApiResponse(responseCode = Constantes.API_STATUS_200, description = Constantes.MSG_API_200,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = @ArraySchema(schema = @Schema(implementation = MaestroResponse.class)))),
        @ApiResponse(responseCode = Constantes.API_STATUS_400, description = Constantes.API_STATUS_400,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class))),
        @ApiResponse(responseCode = Constantes.API_STATUS_500, description = Constantes.MSF_API_500,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class)))
    })
    public List<MaestroResponse> listarRoles() {
        return comunesService.listarRoles();
    }

    @GetMapping(value = "categorias", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar categorias", description = "Listar categorias")
    public List<MaestroResponse> listarCategorias() {
        return comunesService.listarCategorias();
    }

    @GetMapping(value = "ubicaciones", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar ubicaciones", description = "Listar ubicaciones")
    public List<MaestroResponse> listarUbicaciones() {
        return comunesService.listarUbicaciones();
    }

    @GetMapping(value = "severidades", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar severidades", description = "Listar severidades")
    public List<MaestroResponse> listarSeveridades() {
        return comunesService.listarSeveridades();
    }

    @GetMapping(value = "prioridades", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar prioridades", description = "Listar prioridades")
    public List<MaestroResponse> listarPrioridades() {
        return comunesService.listarPrioridades();
    }

    @GetMapping(value = "estados-incidencia", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar estados de incidencia", description = "Listar estados de incidencia")
    public List<MaestroResponse> listarEstadosIncidencia() {
        return comunesService.listarEstadosIncidencia();
    }
}
