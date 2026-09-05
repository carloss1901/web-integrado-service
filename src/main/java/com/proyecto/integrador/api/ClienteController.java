package com.proyecto.integrador.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cliente Controller")
@Validated
@RestController
@RequestMapping("/cliente")
public class ClienteController {
}
