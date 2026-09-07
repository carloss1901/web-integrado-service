package com.proyecto.integrador.api;

import com.proyecto.integrador.model.enums.TipoMaestro;
import com.proyecto.integrador.model.request.comunes.MantenerMaestroRequest;
import com.proyecto.integrador.model.response.MaestroResponse;
import com.proyecto.integrador.service.ComunesService;
import com.proyecto.integrador.util.MessageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Supplier;

@Tag(name = "Comunes Controller")
@Validated
@RestController
@RequestMapping("/comunes")
public class ComunesController {

    @Autowired
    private ComunesService comunesService;

    @GetMapping(value = "roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaestroResponse> listarRoles() {
        return comunesService.listarRoles();
    }

    @GetMapping(value = "categorias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaestroResponse> listarCategorias() {
        return comunesService.listarCategorias();
    }

    @GetMapping(value = "ubicaciones", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaestroResponse> listarUbicaciones() {
        return comunesService.listarUbicaciones();
    }

    @GetMapping(value = "severidades", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaestroResponse> listarSeveridades() {
        return comunesService.listarSeveridades();
    }

    @GetMapping(value = "prioridades", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaestroResponse> listarPrioridades() {
        return comunesService.listarPrioridades();
    }

    @GetMapping(value = "estados-incidencia", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaestroResponse> listarEstadosIncidencia() {
        return comunesService.listarEstadosIncidencia();
    }

    @GetMapping(value = "sla", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaestroResponse> listarSla() {
        return comunesService.listarSla();
    }

    @PostMapping(value = "{maestro}/registrar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarMaestro(
        @PathVariable("maestro") String maestro,
        @RequestBody MantenerMaestroRequest request
    ) {
        return dispatch(() -> comunesService.registrarMaestro(TipoMaestro.fromValor(maestro), request));
    }

    @PutMapping(value = "{maestro}/actualizar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> actualizarMaestro(
        @PathVariable("maestro") String maestro,
        @RequestBody MantenerMaestroRequest request
    ) {
        return dispatch(() -> comunesService.actualizarMaestro(TipoMaestro.fromValor(maestro), request));
    }

    @DeleteMapping(value = "{maestro}/eliminar/{idMaestro}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> eliminarMaestro(
        @PathVariable("maestro") String maestro,
        @PathVariable("idMaestro") Integer idMaestro
    ) {
        return dispatch(() -> comunesService.eliminarMaestro(TipoMaestro.fromValor(maestro), idMaestro));
    }

    private ResponseEntity<Object> dispatch(Supplier<ResponseEntity<Object>> operation) {
        try {
            return operation.get();
        } catch (IllegalArgumentException ex) {
            return MessageResponse.setResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}