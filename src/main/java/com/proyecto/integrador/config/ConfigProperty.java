package com.proyecto.integrador.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(
    ignoreResourceNotFound = true,
    value = "classpath:messages/messagestransactions.properties")
public class ConfigProperty {
    @Autowired
    private Environment env;

    /**
     * Obtiene el valor de una propiedad específica a partir del archivo de propiedades cargado.
     *
     * @param pPropertyKey La clave de la propiedad que se desea obtener.
     * @return El valor asociado a la clave proporcionada, o null si no existe.
     */
    public String getMessage(String pPropertyKey) {
        return env.getProperty(pPropertyKey);
    }
}
