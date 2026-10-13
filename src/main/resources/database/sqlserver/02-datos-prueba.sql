/*
    CampusSeguro 360
    Datos demostrativos para Microsoft SQL Server 2019

    Usuarios de prueba:
    admin        / 123456
    operador     / 123456
    responsable / 123456
    reportante   / 123456
    supervisor   / 123456

    Ejecutar solamente después de 01-esquema.sql
    y sobre una base sin datos.
*/

USE CAMPUSSEGURO360;
GO

SET XACT_ABORT ON;
BEGIN TRANSACTION;

SET IDENTITY_INSERT ROL ON;

INSERT INTO ROL (
    ID_ROL,
    NOMBRE,
    ACTIVO
)
VALUES
    (1, 'ADMINISTRADOR', 1),
    (2, 'OPERADOR_MESA_CONTROL', 1),
    (3, 'RESPONSABLE_ATENCION', 1),
    (4, 'REPORTANTE', 1),
    (5, 'SUPERVISOR', 1);

SET IDENTITY_INSERT ROL OFF;

SET IDENTITY_INSERT USUARIO ON;

INSERT INTO USUARIO (
    ID_USUARIO,
    USUARIO,
    NOMBRES,
    APELLIDOS,
    CORREO,
    PASSWORD,
    ESTADO,
    ACTIVO
)
VALUES
(
    1,
    'admin',
    'Admin',
    'Sistema',
    'admin@demo.com',
    '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre',
    1,
    1
),
(
    2,
    'operador',
    'Operador',
    'Mesa Control',
    'operador@demo.com',
    '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre',
    1,
    1
),
(
    3,
    'responsable',
    'Responsable',
    'Atencion',
    'responsable@demo.com',
    '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre',
    1,
    1
),
(
    4,
    'reportante',
    'Usuario',
    'Reportante',
    'reportante@demo.com',
    '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre',
    1,
    1
),
(
    5,
    'supervisor',
    'Supervisor',
    'Incidencias',
    'supervisor@demo.com',
    '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre',
    1,
    1
);

SET IDENTITY_INSERT USUARIO OFF;

SET IDENTITY_INSERT USUARIO_ROL ON;

INSERT INTO USUARIO_ROL (
    ID_USUARIO_ROL,
    ID_USUARIO,
    ID_ROL,
    ESTADO,
    ACTIVO
)
VALUES
    (1, 1, 1, 1, 1),
    (2, 2, 2, 1, 1),
    (3, 3, 3, 1, 1),
    (4, 4, 4, 1, 1),
    (5, 5, 5, 1, 1);

SET IDENTITY_INSERT USUARIO_ROL OFF;

SET IDENTITY_INSERT CATEGORIA ON;

INSERT INTO CATEGORIA (
    ID_CATEGORIA,
    NOMBRE,
    DESCRIPCION,
    ACTIVO
)
VALUES
(
    1,
    'HARDWARE',
    'Incidencias relacionadas con equipos fisicos y componentes.',
    1
),
(
    2,
    'SOFTWARE',
    'Problemas con sistemas operativos y aplicaciones.',
    1
),
(
    3,
    'REDES',
    'Fallas de conectividad, internet, VPN o cableado.',
    1
),
(
    4,
    'ACCESOS',
    'Problemas relacionados con usuarios, claves y permisos.',
    1
),
(
    5,
    'CORREO',
    'Problemas de correo electronico y buzones.',
    1
),
(
    6,
    'IMPRESION',
    'Fallas en impresoras, colas de impresion o escaneo.',
    1
),
(
    7,
    'SEGURIDAD',
    'Eventos relacionados con seguridad informatica.',
    1
),
(
    8,
    'OTROS',
    'Incidencias sin una categoria especifica.',
    1
);

SET IDENTITY_INSERT CATEGORIA OFF;

SET IDENTITY_INSERT UBICACION ON;

INSERT INTO UBICACION (
    ID_UBICACION,
    NOMBRE,
    DESCRIPCION,
    ACTIVO
)
VALUES
    (1, 'SEDE PRINCIPAL', 'Oficina principal.', 1),
    (2, 'AREA ADMINISTRATIVA', 'Oficinas administrativas.', 1),
    (3, 'AREA ACADEMICA', 'Aulas y laboratorios.', 1),
    (4, 'MESA DE AYUDA', 'Area de soporte.', 1),
    (5, 'CENTRO DE DATOS', 'Servidores y comunicaciones.', 1),
    (6, 'ALMACEN', 'Almacenamiento de equipos.', 1),
    (7, 'SALA DE REUNIONES', 'Salas de reuniones.', 1),
    (8, 'REMOTO', 'Atencion remota.', 1);

SET IDENTITY_INSERT UBICACION OFF;

SET IDENTITY_INSERT SEVERIDAD ON;

INSERT INTO SEVERIDAD (
    ID_SEVERIDAD,
    NOMBRE,
    NIVEL,
    ACTIVO
)
VALUES
    (1, 'BAJA', 1, 1),
    (2, 'MEDIA', 2, 1),
    (3, 'ALTA', 3, 1),
    (4, 'CRITICA', 4, 1);

SET IDENTITY_INSERT SEVERIDAD OFF;

SET IDENTITY_INSERT PRIORIDAD ON;

INSERT INTO PRIORIDAD (
    ID_PRIORIDAD,
    NOMBRE,
    NIVEL,
    PUNTAJE_MINIMO,
    PUNTAJE_MAXIMO,
    ACTIVO
)
VALUES
    (1, 'BAJA', 1, 2, 3, 1),
    (2, 'MEDIA', 2, 4, 5, 1),
    (3, 'ALTA', 3, 6, 6, 1),
    (4, 'CRITICA', 4, 7, 7, 1);

SET IDENTITY_INSERT PRIORIDAD OFF;

SET IDENTITY_INSERT ESTADO_INCIDENCIA ON;

INSERT INTO ESTADO_INCIDENCIA (
    ID_ESTADO,
    NOMBRE,
    ACTIVO
)
VALUES
    (1, 'REGISTRADA', 1),
    (2, 'CLASIFICADA', 1),
    (3, 'ASIGNADA', 1),
    (4, 'EN_ATENCION', 1),
    (5, 'EN_ESPERA', 1),
    (6, 'RESUELTA', 1),
    (7, 'CERRADA', 1),
    (8, 'CANCELADA', 1),
    (9, 'REABIERTA', 1);

SET IDENTITY_INSERT ESTADO_INCIDENCIA OFF;

SET IDENTITY_INSERT SLA ON;

INSERT INTO SLA (
    ID_SLA,
    ID_SEVERIDAD,
    MINUTOS_RESOLUCION,
    ACTIVO
)
VALUES
    (1, 1, 4320, 1),
    (2, 2, 1440, 1),
    (3, 3, 480, 1),
    (4, 4, 120, 1);

SET IDENTITY_INSERT SLA OFF;

SET IDENTITY_INSERT INCIDENCIA ON;

INSERT INTO INCIDENCIA (
    ID_INCIDENCIA,
    CODIGO,
    ID_REPORTANTE,
    ID_RESPONSABLE,
    ID_CATEGORIA,
    ID_UBICACION,
    ID_SEVERIDAD,
    ID_PRIORIDAD,
    ID_ESTADO,
    ID_SLA,
    IMPACTO,
    URGENCIA,
    REINCIDENCIA,
    PUNTAJE_PRIORIDAD,
    TITULO,
    DESCRIPCION,
    FECHA_REGISTRO,
    FECHA_LIMITE,
    FECHA_RESOLUCION,
    FECHA_CIERRE,
    ACTIVO
)
VALUES
(
    1,
    'INC-AFP02-001',
    4,
    NULL,
    1,
    1,
    NULL,
    NULL,
    1,
    NULL,
    NULL,
    NULL,
    0,
    NULL,
    'Equipo no enciende',
    N'El equipo de escritorio no muestra imagen.',
    DATEADD(HOUR, -3, SYSDATETIME()),
    NULL,
    NULL,
    NULL,
    1
),
(
    2,
    'INC-AFP02-002',
    4,
    NULL,
    3,
    2,
    3,
    3,
    2,
    3,
    3,
    3,
    0,
    6,
    'Sin conexion a la red',
    N'El usuario no puede conectarse a la red corporativa.',
    DATEADD(DAY, -2, SYSDATETIME()),
    DATEADD(DAY, -1, SYSDATETIME()),
    NULL,
    NULL,
    1
),
(
    3,
    'INC-AFP02-003',
    4,
    3,
    2,
    3,
    2,
    2,
    3,
    2,
    2,
    2,
    0,
    4,
    'Aplicacion no inicia',
    N'La aplicacion institucional se cierra durante el inicio.',
    DATEADD(HOUR, -4, SYSDATETIME()),
    DATEADD(HOUR, 20, SYSDATETIME()),
    NULL,
    NULL,
    1
),
(
    4,
    'INC-AFP02-004',
    4,
    3,
    4,
    2,
    4,
    4,
    4,
    4,
    3,
    3,
    1,
    7,
    'Usuario bloqueado',
    N'La cuenta permanece bloqueada y afecta un proceso critico.',
    DATEADD(HOUR, -6, SYSDATETIME()),
    DATEADD(HOUR, -2, SYSDATETIME()),
    NULL,
    NULL,
    1
),
(
    5,
    'INC-AFP02-005',
    4,
    3,
    6,
    1,
    1,
    1,
    5,
    1,
    1,
    2,
    0,
    3,
    'Impresora sin papel',
    N'La impresora del area administrativa requiere reposicion.',
    DATEADD(HOUR, -2, SYSDATETIME()),
    DATEADD(DAY, 2, SYSDATETIME()),
    NULL,
    NULL,
    1
),
(
    6,
    'INC-AFP02-006',
    4,
    3,
    5,
    8,
    2,
    2,
    6,
    2,
    2,
    2,
    0,
    4,
    'Correo no sincroniza',
    N'El correo fue configurado nuevamente y quedo operativo.',
    DATEADD(DAY, -3, SYSDATETIME()),
    DATEADD(DAY, -2, SYSDATETIME()),
    DATEADD(DAY, -2, SYSDATETIME()),
    NULL,
    1
),
(
    7,
    'INC-AFP02-007',
    4,
    3,
    1,
    4,
    1,
    1,
    7,
    1,
    1,
    1,
    0,
    2,
    'Cambio de teclado',
    N'El teclado defectuoso fue reemplazado y la incidencia cerrada.',
    DATEADD(DAY, -5, SYSDATETIME()),
    DATEADD(DAY, -2, SYSDATETIME()),
    DATEADD(DAY, -3, SYSDATETIME()),
    DATEADD(DAY, -2, SYSDATETIME()),
    1
),
(
    8,
    'INC-AFP02-008',
    4,
    3,
    7,
    5,
    4,
    4,
    9,
    4,
    3,
    3,
    1,
    7,
    'Alerta de seguridad reabierta',
    N'La alerta fue reabierta porque se detecto nuevamente actividad.',
    DATEADD(DAY, -4, SYSDATETIME()),
    DATEADD(DAY, -3, SYSDATETIME()),
    NULL,
    NULL,
    1
);

SET IDENTITY_INSERT INCIDENCIA OFF;

SET IDENTITY_INSERT INCIDENCIA_HISTORIAL ON;

INSERT INTO INCIDENCIA_HISTORIAL (
    ID_HISTORIAL,
    ID_INCIDENCIA,
    ID_USUARIO,
    TIPO_EVENTO,
    VALOR_ANTERIOR,
    VALOR_NUEVO,
    DESCRIPCION,
    FECHA_EVENTO,
    ACTIVO
)
VALUES
(
    1,
    1,
    4,
    'REGISTRO_INCIDENCIA',
    NULL,
    'REGISTRADA',
    'Incidencia registrada por el reportante.',
    DATEADD(HOUR, -3, SYSDATETIME()),
    1
),
(
    2,
    3,
    2,
    'ASIGNACION_RESPONSABLE',
    'SIN_RESPONSABLE',
    'RESPONSABLE_ATENCION',
    'Incidencia asignada al responsable de atencion.',
    DATEADD(HOUR, -3, SYSDATETIME()),
    1
),
(
    3,
    6,
    3,
    'CAMBIO_ESTADO',
    'EN_ATENCION',
    'RESUELTA',
    'Incidencia solucionada correctamente.',
    DATEADD(DAY, -2, SYSDATETIME()),
    1
);

SET IDENTITY_INSERT INCIDENCIA_HISTORIAL OFF;

SET IDENTITY_INSERT INCIDENCIA_EVIDENCIA ON;

INSERT INTO INCIDENCIA_EVIDENCIA (
    ID_EVIDENCIA,
    ID_INCIDENCIA,
    ID_USUARIO,
    NOMBRE_ARCHIVO,
    RUTA_ARCHIVO,
    DESCRIPCION,
    FECHA_REGISTRO,
    ACTIVO
)
VALUES
(
    1,
    3,
    3,
    'captura-error.png',
    '/evidencias/captura-error.png',
    'Captura del error mostrado por la aplicacion.',
    DATEADD(HOUR, -2, SYSDATETIME()),
    1
);

SET IDENTITY_INSERT INCIDENCIA_EVIDENCIA OFF;

SET IDENTITY_INSERT INCIDENCIA_ACCION_CORRECTIVA ON;

INSERT INTO INCIDENCIA_ACCION_CORRECTIVA (
    ID_ACCION_CORRECTIVA,
    ID_INCIDENCIA,
    ID_USUARIO,
    DESCRIPCION,
    FECHA_REGISTRO,
    ACTIVO
)
VALUES
(
    1,
    6,
    3,
    N'Se elimino y configuro nuevamente el perfil de correo.',
    DATEADD(DAY, -2, SYSDATETIME()),
    1
);

SET IDENTITY_INSERT INCIDENCIA_ACCION_CORRECTIVA OFF;

SET IDENTITY_INSERT INCIDENCIA_COMENTARIO ON;

INSERT INTO INCIDENCIA_COMENTARIO (
    ID_COMENTARIO,
    ID_INCIDENCIA,
    ID_USUARIO,
    COMENTARIO,
    FECHA_REGISTRO,
    ACTIVO
)
VALUES
(
    1,
    3,
    3,
    N'Se coordino una revision remota con el usuario.',
    DATEADD(HOUR, -2, SYSDATETIME()),
    1
);

SET IDENTITY_INSERT INCIDENCIA_COMENTARIO OFF;

COMMIT TRANSACTION;
GO