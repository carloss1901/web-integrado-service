package com.proyecto.integrador.api;

import com.proyecto.integrador.model.request.usuario.ActualizarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.ListarUsuarioRequest;
import com.proyecto.integrador.model.request.usuario.RegistrarUsuarioRequest;
import com.proyecto.integrador.model.projection.UsuarioProjection;
import com.proyecto.integrador.service.UsuarioService;
import com.proyecto.integrador.util.CustomPage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<Object> obtenerUsuario(@PathVariable("idUsuario") Integer idUsuario) {
        return usuarioService.obtenerUsuario(idUsuario);
    }

    @PostMapping(value = "registrar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarUsuario(@Valid @RequestBody RegistrarUsuarioRequest request) {
        return usuarioService.registrarUsuario(request);
    }

    @PutMapping(value = "actualizar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> actualizarUsuario(@Valid @RequestBody ActualizarUsuarioRequest request) {
        return usuarioService.actualizarUsuario(request);
    }

    @DeleteMapping(value = "eliminar/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> eliminarUsuario(@PathVariable("idUsuario") Integer idUsuario) {
        return usuarioService.eliminarUsuario(idUsuario);
    }
}