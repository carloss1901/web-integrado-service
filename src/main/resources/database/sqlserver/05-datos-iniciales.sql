/*
    CampusSeguro 360
    Datos iniciales para una base existente.

    Incluye roles, usuarios de acceso, catálogos y SLA.
    No inserta incidencias ni datos de trazabilidad.
    Puede ejecutarse más de una vez sin duplicar registros.
*/

USE CAMPUSSEGURO360;
GO

SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    SET IDENTITY_INSERT ROL ON;
    INSERT INTO ROL (ID_ROL, NOMBRE, ACTIVO)
    SELECT v.ID_ROL, v.NOMBRE, 1
    FROM (VALUES
        (1, 'REPORTANTE'),
        (2, 'OPERADOR_MESA_CONTROL'),
        (3, 'RESPONSABLE_ATENCION'),
        (4, 'SUPERVISOR'),
        (5, 'ADMINISTRADOR')
    ) v(ID_ROL, NOMBRE)
    WHERE NOT EXISTS (
        SELECT 1 FROM ROL r WHERE r.ID_ROL = v.ID_ROL OR r.NOMBRE = v.NOMBRE
    );
    SET IDENTITY_INSERT ROL OFF;

    /* Password de demostración: 123456 */
    SET IDENTITY_INSERT USUARIO ON;
    INSERT INTO USUARIO (
        ID_USUARIO, USUARIO, NOMBRES, APELLIDOS, CORREO, PASSWORD, ESTADO, ACTIVO
    )
    SELECT v.ID_USUARIO, v.USUARIO, v.NOMBRES, v.APELLIDOS, v.CORREO,
           v.PASSWORD, 1, 1
    FROM (VALUES
        (1, 'admin', 'Admin', 'Sistema', 'admin@demo.com', '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre'),
        (2, 'operador', 'Operador', 'Mesa Control', 'operador@demo.com', '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre'),
        (3, 'responsable', 'Responsable', 'Atencion', 'responsable@demo.com', '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre'),
        (4, 'reportante', 'Usuario', 'Reportante', 'reportante@demo.com', '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre'),
        (5, 'supervisor', 'Supervisor', 'Incidencias', 'supervisor@demo.com', '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre')
    ) v(ID_USUARIO, USUARIO, NOMBRES, APELLIDOS, CORREO, PASSWORD)
    WHERE NOT EXISTS (
        SELECT 1 FROM USUARIO u
        WHERE u.ID_USUARIO = v.ID_USUARIO OR u.USUARIO = v.USUARIO OR u.CORREO = v.CORREO
    );
    SET IDENTITY_INSERT USUARIO OFF;

    SET IDENTITY_INSERT USUARIO_ROL ON;
    INSERT INTO USUARIO_ROL (ID_USUARIO_ROL, ID_USUARIO, ID_ROL, ESTADO, ACTIVO)
    SELECT v.ID_USUARIO_ROL, v.ID_USUARIO, v.ID_ROL, 1, 1
    FROM (VALUES
        (1, 1, 5),
        (2, 2, 2),
        (3, 3, 3),
        (4, 4, 1),
        (5, 5, 4)
    ) v(ID_USUARIO_ROL, ID_USUARIO, ID_ROL)
    WHERE NOT EXISTS (
        SELECT 1 FROM USUARIO_ROL ur
        WHERE ur.ID_USUARIO = v.ID_USUARIO AND ur.ID_ROL = v.ID_ROL
    );
    SET IDENTITY_INSERT USUARIO_ROL OFF;

    SET IDENTITY_INSERT CATEGORIA ON;
    INSERT INTO CATEGORIA (ID_CATEGORIA, NOMBRE, DESCRIPCION, ACTIVO)
    SELECT v.ID_CATEGORIA, v.NOMBRE, v.DESCRIPCION, 1
    FROM (VALUES
        (1, 'HARDWARE', 'Incidencias relacionadas con equipos fisicos y componentes.'),
        (2, 'SOFTWARE', 'Problemas con sistemas operativos y aplicaciones.'),
        (3, 'REDES', 'Fallas de conectividad, internet, VPN o cableado.'),
        (4, 'ACCESOS', 'Problemas relacionados con usuarios, claves y permisos.'),
        (5, 'CORREO', 'Problemas de correo electronico y buzones.'),
        (6, 'IMPRESION', 'Fallas en impresoras, colas de impresion o escaneo.'),
        (7, 'SEGURIDAD', 'Eventos relacionados con seguridad informatica.'),
        (8, 'OTROS', 'Incidencias sin una categoria especifica.')
    ) v(ID_CATEGORIA, NOMBRE, DESCRIPCION)
    WHERE NOT EXISTS (SELECT 1 FROM CATEGORIA c WHERE c.ID_CATEGORIA = v.ID_CATEGORIA OR c.NOMBRE = v.NOMBRE);
    SET IDENTITY_INSERT CATEGORIA OFF;

    SET IDENTITY_INSERT UBICACION ON;
    INSERT INTO UBICACION (ID_UBICACION, NOMBRE, DESCRIPCION, ACTIVO)
    SELECT v.ID_UBICACION, v.NOMBRE, v.DESCRIPCION, 1
    FROM (VALUES
        (1, 'SEDE PRINCIPAL', 'Oficina principal.'),
        (2, 'AREA ADMINISTRATIVA', 'Oficinas administrativas.'),
        (3, 'AREA ACADEMICA', 'Aulas y laboratorios.'),
        (4, 'MESA DE AYUDA', 'Area de soporte.'),
        (5, 'CENTRO DE DATOS', 'Servidores y comunicaciones.'),
        (6, 'ALMACEN', 'Almacenamiento de equipos.'),
        (7, 'SALA DE REUNIONES', 'Salas de reuniones.'),
        (8, 'REMOTO', 'Atencion remota.')
    ) v(ID_UBICACION, NOMBRE, DESCRIPCION)
    WHERE NOT EXISTS (SELECT 1 FROM UBICACION u WHERE u.ID_UBICACION = v.ID_UBICACION OR u.NOMBRE = v.NOMBRE);
    SET IDENTITY_INSERT UBICACION OFF;

    SET IDENTITY_INSERT SEVERIDAD ON;
    INSERT INTO SEVERIDAD (ID_SEVERIDAD, NOMBRE, NIVEL, ACTIVO)
    SELECT v.ID_SEVERIDAD, v.NOMBRE, v.NIVEL, 1
    FROM (VALUES
        (1, 'BAJA', 1),
        (2, 'MEDIA', 2),
        (3, 'ALTA', 3),
        (4, 'CRITICA', 4)
    ) v(ID_SEVERIDAD, NOMBRE, NIVEL)
    WHERE NOT EXISTS (SELECT 1 FROM SEVERIDAD s WHERE s.ID_SEVERIDAD = v.ID_SEVERIDAD OR s.NOMBRE = v.NOMBRE);
    SET IDENTITY_INSERT SEVERIDAD OFF;

    SET IDENTITY_INSERT PRIORIDAD ON;
    INSERT INTO PRIORIDAD (ID_PRIORIDAD, NOMBRE, NIVEL, PUNTAJE_MINIMO, PUNTAJE_MAXIMO, ACTIVO)
    SELECT v.ID_PRIORIDAD, v.NOMBRE, v.NIVEL, v.PUNTAJE_MINIMO, v.PUNTAJE_MAXIMO, 1
    FROM (VALUES
        (1, 'BAJA', 1, CAST(1.00 AS DECIMAL(4,2)), CAST(1.75 AS DECIMAL(4,2))),
        (2, 'MEDIA', 2, CAST(1.75 AS DECIMAL(4,2)), CAST(2.50 AS DECIMAL(4,2))),
        (3, 'ALTA', 3, CAST(2.50 AS DECIMAL(4,2)), CAST(3.25 AS DECIMAL(4,2))),
        (4, 'CRITICA', 4, CAST(3.25 AS DECIMAL(4,2)), CAST(4.00 AS DECIMAL(4,2)))
    ) v(ID_PRIORIDAD, NOMBRE, NIVEL, PUNTAJE_MINIMO, PUNTAJE_MAXIMO)
    WHERE NOT EXISTS (SELECT 1 FROM PRIORIDAD p WHERE p.ID_PRIORIDAD = v.ID_PRIORIDAD OR p.NOMBRE = v.NOMBRE);
    SET IDENTITY_INSERT PRIORIDAD OFF;

    SET IDENTITY_INSERT ESTADO_INCIDENCIA ON;
    INSERT INTO ESTADO_INCIDENCIA (ID_ESTADO, NOMBRE, ACTIVO)
    SELECT v.ID_ESTADO, v.NOMBRE, 1
    FROM (VALUES
        (1, 'REGISTRADA'),
        (2, 'CLASIFICADA'),
        (3, 'ASIGNADA'),
        (4, 'EN_ATENCION'),
        (5, 'EN_ESPERA'),
        (6, 'RESUELTA'),
        (7, 'CERRADA'),
        (8, 'CANCELADA'),
        (9, 'REABIERTA')
    ) v(ID_ESTADO, NOMBRE)
    WHERE NOT EXISTS (SELECT 1 FROM ESTADO_INCIDENCIA e WHERE e.ID_ESTADO = v.ID_ESTADO OR e.NOMBRE = v.NOMBRE);
    SET IDENTITY_INSERT ESTADO_INCIDENCIA OFF;

    SET IDENTITY_INSERT SLA ON;
    INSERT INTO SLA (ID_SLA, ID_SEVERIDAD, MINUTOS_RESOLUCION, ACTIVO)
    SELECT v.ID_SLA, v.ID_SEVERIDAD, v.MINUTOS_RESOLUCION, 1
    FROM (VALUES
        (1, 1, 4320),
        (2, 2, 1440),
        (3, 3, 480),
        (4, 4, 120)
    ) v(ID_SLA, ID_SEVERIDAD, MINUTOS_RESOLUCION)
    WHERE NOT EXISTS (
        SELECT 1 FROM SLA s WHERE s.ID_SLA = v.ID_SLA OR s.ID_SEVERIDAD = v.ID_SEVERIDAD
    );
    SET IDENTITY_INSERT SLA OFF;

    COMMIT TRANSACTION;
    PRINT 'Datos iniciales registrados correctamente.';
END TRY
BEGIN CATCH
    IF XACT_STATE() <> 0
        ROLLBACK TRANSACTION;

    IF OBJECTPROPERTY(OBJECT_ID('ROL'), 'TableHasIdentity') = 1 SET IDENTITY_INSERT ROL OFF;
    IF OBJECTPROPERTY(OBJECT_ID('USUARIO'), 'TableHasIdentity') = 1 SET IDENTITY_INSERT USUARIO OFF;
    IF OBJECTPROPERTY(OBJECT_ID('USUARIO_ROL'), 'TableHasIdentity') = 1 SET IDENTITY_INSERT USUARIO_ROL OFF;
    IF OBJECTPROPERTY(OBJECT_ID('CATEGORIA'), 'TableHasIdentity') = 1 SET IDENTITY_INSERT CATEGORIA OFF;
    IF OBJECTPROPERTY(OBJECT_ID('UBICACION'), 'TableHasIdentity') = 1 SET IDENTITY_INSERT UBICACION OFF;
    IF OBJECTPROPERTY(OBJECT_ID('SEVERIDAD'), 'TableHasIdentity') = 1 SET IDENTITY_INSERT SEVERIDAD OFF;
    IF OBJECTPROPERTY(OBJECT_ID('PRIORIDAD'), 'TableHasIdentity') = 1 SET IDENTITY_INSERT PRIORIDAD OFF;
    IF OBJECTPROPERTY(OBJECT_ID('ESTADO_INCIDENCIA'), 'TableHasIdentity') = 1 SET IDENTITY_INSERT ESTADO_INCIDENCIA OFF;
    IF OBJECTPROPERTY(OBJECT_ID('SLA'), 'TableHasIdentity') = 1 SET IDENTITY_INSERT SLA OFF;

    THROW;
END CATCH;
GO
