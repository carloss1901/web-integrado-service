package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.usuario.ActualizarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.ListarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.model.projection.UsuarioProjection;
import com.proyecto.integrador.model.response.UsuarioResponse;
import com.proyecto.integrador.service.UsuarioService;
import com.proyecto.integrador.util.Constantes;
import com.proyecto.integrador.util.CustomPage;
import com.proyecto.integrador.util.ErrorGenerico;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuario Controller")
@Validated
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "listar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar usuarios", description = "Listar usuarios", responses = {
        @ApiResponse(responseCode = Constantes.API_STATUS_200, description = Constantes.MSG_API_200,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UsuarioProjection.class))),
        @ApiResponse(responseCode = Constantes.API_STATUS_400, description = Constantes.MSG_API_400,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class))),
        @ApiResponse(responseCode = Constantes.API_STATUS_500, description = Constantes.MSF_API_500,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class)))
    })
    public CustomPage<UsuarioProjection> listarUsuario(
        @RequestParam(value = "usuario", required = false) String usuario,
        @RequestParam(value = "nombres", required = false) String nombres,
        @RequestParam(value = "apellidos", required = false) String apellidos,
        @RequestParam(value = "correo", required = false) String correo,
        @RequestParam(value = "estado", required = false) Short estado,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @Min(value = 1, message = "{message.minOne}")
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        ListarUsuarioRequest rq = new ListarUsuarioRequest();
        rq.setUsuario(usuario);
        rq.setNombres(nombres);
        rq.setApellidos(apellidos);
        rq.setCorreo(correo);
        rq.setEstado(estado);
        rq.setPage(page);
        rq.setSize(size);

        return usuarioService.listarUsuario(rq);
    }

    @GetMapping(value = "{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener usuario", description = "Obtener datos del usuario por id", responses = {
        @ApiResponse(responseCode = Constantes.API_STATUS_200, description = Constantes.MSG_API_200,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UsuarioResponse.class))),
        @ApiResponse(responseCode = Constantes.API_STATUS_400, description = Constantes.MSG_API_400,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class))),
        @ApiResponse(responseCode = Constantes.API_STATUS_500, description = Constantes.MSF_API_500,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorGenerico.class)))
    })
    public ResponseEntity<Object> obtenerUsuario(@PathVariable("idUsuario") Integer idUsuario) {
        return usuarioService.obtenerUsuario(idUsuario);
    }

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
    public ResponseEntity<Object> registrarUsuario(@Valid @RequestBody RegistrarUsuarioRequest request) {
        return usuarioService.registrarUsuario(request);
    }

    @PutMapping(value = "actualizar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar usuario", description = "Actualizar usuario y sus roles", responses = {
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
    public ResponseEntity<Object> actualizarUsuario(@Valid @RequestBody ActualizarUsuarioRequest request) {
        return usuarioService.actualizarUsuario(request);
    }

    @DeleteMapping(value = "eliminar/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Eliminar usuario", description = "Eliminar usuario de forma logica", responses = {
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
    public ResponseEntity<Object> eliminarUsuario(@PathVariable("idUsuario") Integer idUsuario) {
        return usuarioService.eliminarUsuario(idUsuario);
    }
}
