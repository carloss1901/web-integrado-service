package com.proyecto.integrador.util;

import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Funciones de compatibilidad con MySQL para la base de datos embebida H2.
 * H2 no incluye DATE_FORMAT de MySQL, por lo que se registra una funcion
 * equivalente a traves de una clase Java.
 */
@Component
public class H2Functions {

    /**
     * Emula la funcion MySQL DATE_FORMAT(date, format) usando SimpleDateFormat.
     * Soporta los tokens mas habituales de MySQL.
     */
    public static String dateFormat(Date date, String format) {
        if (date == null) {
            return null;
        }
        String javaFormat = format;
        javaFormat = javaFormat.replace("%d", "dd");
        javaFormat = javaFormat.replace("%m", "MM");
        javaFormat = javaFormat.replace("%Y", "yyyy");
        javaFormat = javaFormat.replace("%y", "yy");
        javaFormat = javaFormat.replace("%H", "HH");
        javaFormat = javaFormat.replace("%M", "mm");
        javaFormat = javaFormat.replace("%S", "ss");
        javaFormat = javaFormat.replace("%i", "mm");
        javaFormat = javaFormat.replace("%e", "d");
        javaFormat = javaFormat.replace("%c", "M");
        return new SimpleDateFormat(javaFormat).format(date);
    }
}
