# web-integrado-service

Servicio de gestión web integrado (Spring Boot).

## Descripción

Servicio backend del sistema web integrado, construido como réplica simplificada del proyecto `integrador-service` con un endpoint de ejemplo de clientes.

## Stack

- Java 17
- Spring Boot 3.0.6
- Spring Data JPA
- MySQL 8
- Swagger / OpenAPI

## Endpoint de ejemplo

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/cliente/listar` | Listar clientes (con filtros y paginación) |
| POST | `/cliente/registrar` | Registrar o editar cliente |
| DELETE | `/cliente/desactivar` | Activar / desactivar cliente |

Documentación Swagger disponible en `/swagger-ui.html`.

## Instalación

```bash
mvnw clean install
```

## Ejecución

```bash
mvnw spring-boot:run
```

## Licencia

MIT
