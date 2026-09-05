package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.service.UsuarioService;
import com.proyecto.integrador.util.Constantes;
import com.proyecto.integrador.util.ErrorGenerico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuario Controller")
@Validated
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping(value = "registrar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registrar usuario", description = "Registrar usuario", responses = {
        @ApiResponse(responseCode = Constantes.API_STATUS_200, description = Constantes.MSG_API_200,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ResponseEntity.class))),
        @ApiResponse(responseCode = Constantes.API_STATUS_400, description = Constantes.MSG_API_400,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class))),
        @ApiResponse(responseCode = Constantes.API_STATUS_500, description = Constantes.MSF_API_500,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class)))
    })
    public ResponseEntity<Object> registrarUsuario(@RequestBody RegistrarUsuarioRequest request) {
        return usuarioService.registrarUsuario(request);
    }
}
