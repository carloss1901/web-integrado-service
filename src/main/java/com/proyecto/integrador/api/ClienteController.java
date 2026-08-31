package com.proyecto.integrador.api;

import com.proyecto.integrador.model.projection.ClienteProjection;
import com.proyecto.integrador.model.request.cliente.DesactivarClienteRequest;
import com.proyecto.integrador.model.request.cliente.ListarClienteRequest;
import com.proyecto.integrador.model.request.cliente.RegistrarClienteRequest;
import com.proyecto.integrador.service.ClienteService;
import com.proyecto.integrador.util.Constantes;
import com.proyecto.integrador.util.CustomPage;
import com.proyecto.integrador.util.ErrorGenerico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cliente Controller")
@Validated
@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping(value = "listar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar clientes",
        description = "Listar clientes",
        responses = {
            @ApiResponse(
                responseCode = Constantes.API_STATUS_200,
                description = Constantes.MSG_API_200,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema =
                        @Schema(implementation = ClienteProjection.class)))),
            @ApiResponse(
                responseCode = Constantes.API_STATUS_400,
                description = Constantes.API_STATUS_400,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorGenerico.class))),
            @ApiResponse(
                responseCode = Constantes.API_STATUS_500,
                description = Constantes.MSF_API_500,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorGenerico.class)))
    })
    public CustomPage<ClienteProjection> listarCliente(
        @RequestParam(value = "nombre", required = false) String nombre,
        @RequestParam(value = "cod_externo", required = false) String codExterno,
        @RequestParam(value = "estado", required = false) Short estado,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        ListarClienteRequest rq = new ListarClienteRequest();
        rq.setNombre(nombre);
        rq.setCodExterno(codExterno);
        rq.setEstado(estado);
        rq.setPage(page);
        rq.setSize(size);

        return clienteService.listarCliente(rq);
    }

    @PostMapping(value = "registrar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registrar y editar clientes",
        description = "Registrar y editar clientes")
    public ResponseEntity<Object> registrarCliente(@RequestBody RegistrarClienteRequest request) {
        return clienteService.registrarCliente(request);
    }

    @DeleteMapping(value = "desactivar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Desactivar y activar clientes",
        description = "Desactivar y activar clientes")
    public ResponseEntity<Object> desactivarCliente(@RequestBody DesactivarClienteRequest request) {
        return clienteService.desactivarCliente(request);
    }
}
