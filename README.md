# CampusSeguro 360 - Backend

Backend para la gestión de incidencias de CampusSeguro 360, desarrollado para el curso Desarrollo Web Integrado de la UTP.

El sistema permite registrar, clasificar, asignar, atender, resolver y cerrar incidencias. También administra usuarios, roles, evidencias, comentarios, acciones correctivas, historial e indicadores.

## Tecnologías

- Java 17
- Spring Boot 3.0.6
- Spring Data JPA / Hibernate
- Spring Security
- JWT
- SQL Server 2019
- H2 para desarrollo y pruebas
- Swagger / OpenAPI
- Maven
- JUnit 5
- Mockito

## Arquitectura

El proyecto utiliza una arquitectura por capas:

```text
api
 └── Controladores REST

service
 └── Reglas de negocio y transacciones

repository
 └── Persistencia y consultas JPQL

model
 ├── entity
 ├── request
 ├── response
 └── projection

security
 └── Autenticación JWT y autorización por roles

config
 └── Seguridad, CORS y Swagger
```

## Roles del sistema

- `ADMINISTRADOR`
- `OPERADOR_MESA_CONTROL`
- `RESPONSABLE_ATENCION`
- `REPORTANTE`
- `SUPERVISOR`

Cada operación del flujo de incidencias está protegida según el rol autenticado.

## Base de datos

El entorno de producción utiliza Microsoft SQL Server 2019.

Los scripts se encuentran en:

```text
src/main/resources/database/sqlserver/01-esquema.sql
src/main/resources/database/sqlserver/02-datos-prueba.sql
```

Orden de ejecución sobre una base vacía:

1. `01-esquema.sql`
2. `02-datos-prueba.sql`

Los scripts crean:

- 14 tablas relacionadas.
- Claves primarias y foráneas.
- Restricciones e índices.
- Catálogos.
- Cinco usuarios demostrativos.
- Ocho incidencias de prueba.
- Historial, evidencias, comentarios y acciones correctivas.

## Perfiles

### Desarrollo

El perfil predeterminado es `dev`.

Utiliza una base H2 en memoria y habilita Swagger:

```powershell
mvn spring-boot:run
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

### Producción

El perfil `prod` utiliza SQL Server y obtiene la configuración desde variables de entorno.

Ejemplo en PowerShell:

```powershell
$env:DB_URL = "jdbc:sqlserver://SERVIDOR:PUERTO;databaseName=CAMPUSSEGURO360;encrypt=true;trustServerCertificate=true"
$env:DB_USERNAME = "USUARIO_SQL"

$sqlPasswordSeguro = Read-Host "Ingrese la contraseña de SQL Server" -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $sqlPasswordSeguro).Password

$env:JWT_SECRET = "SECRETO_JWT_SEGURO"
$env:CORS_ALLOWED_ORIGINS = "http://localhost:5173"
$env:SPRING_PROFILES_ACTIVE = "prod"

mvn spring-boot:run
```

No se deben guardar contraseñas, tokens o secretos reales dentro del repositorio.

Swagger permanece deshabilitado normalmente en producción.

## Autenticación

Inicio de sesión:

```http
POST /auth/login
```

Ejemplo:

```json
{
  "usuario": "admin",
  "contrasena": "123456"
}
```

La respuesta entrega un JWT.

Para consumir endpoints protegidos se envía:

```http
Authorization: Bearer TOKEN_JWT
```

Las credenciales incluidas en los datos de prueba son solamente demostrativas y no deben utilizarse en un ambiente real.

## Endpoints principales

### Autenticación

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/auth/login` | Iniciar sesión y generar JWT |

### Usuarios

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/usuario/listar` | Listar usuarios |
| GET | `/usuario/{idUsuario}` | Obtener usuario |
| POST | `/usuario/registrar` | Registrar usuario |
| PUT | `/usuario/actualizar` | Actualizar usuario |
| DELETE | `/usuario/eliminar/{idUsuario}` | Desactivar usuario |

### Incidencias

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/incidencias/listar` | Listar incidencias |
| GET | `/incidencias/{idIncidencia}` | Obtener detalle |
| GET | `/incidencias/buscar` | Buscar por título |
| GET | `/incidencias/buscar-por-fechas` | Buscar por fechas |
| GET | `/incidencias/vencidas` | Listar incidencias vencidas |
| GET | `/incidencias/indicadores` | Consultar indicadores |
| POST | `/incidencias/registrar` | Registrar incidencia |
| PUT | `/incidencias/actualizar` | Actualizar incidencia |
| DELETE | `/incidencias/eliminar/{idIncidencia}` | Eliminar incidencia |
| POST | `/incidencias/clasificar` | Clasificar incidencia |
| POST | `/incidencias/asignar-responsable` | Asignar responsable |
| POST | `/incidencias/iniciar-atencion` | Iniciar atención |
| POST | `/incidencias/resolver` | Resolver incidencia |
| POST | `/incidencias/cerrar` | Cerrar incidencia |
| POST | `/incidencias/evidencias` | Registrar evidencia |
| POST | `/incidencias/acciones-correctivas` | Registrar acción correctiva |
| POST | `/incidencias/comentarios` | Registrar comentario |

### Información relacionada

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/incidencias/{id}/historial` | Consultar historial |
| GET | `/incidencias/{id}/evidencias` | Consultar evidencias |
| GET | `/incidencias/{id}/acciones-correctivas` | Consultar acciones correctivas |
| GET | `/incidencias/{id}/comentarios` | Consultar comentarios |

## Indicadores

El endpoint:

```http
GET /incidencias/indicadores
```

calcula directamente desde la persistencia:

- Total de incidencias.
- Incidencias pendientes.
- Incidencias vencidas.
- Incidencias resueltas.
- Incidencias cerradas.

Este endpoint está autorizado para:

- Administrador.
- Operador de mesa de control.
- Supervisor.

## Pruebas

Ejecutar:

```powershell
mvn test
```

Resultado verificado:

```text
Tests run: 62
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Las pruebas cubren:

- Controladores.
- Servicios.
- Repositorios JPQL.
- JWT.
- Autorización por roles.
- CORS para React.
- Indicadores calculados desde persistencia.

## Frontend previsto

El frontend se desarrollará con:

- React
- TypeScript
- Vite

Origen permitido durante desarrollo:

```text
http://localhost:5173
```