/*
    CampusSeguro 360 - datos para estadisticas y pruebas de flujo.

    Requisitos:
    1. Ejecutar 05-datos-iniciales.sql antes de este script.
    2. SQL Server 2019 o superior.

    Caracteristicas:
    - Inserta 2,000 incidencias identificadas con el prefijo EST-.
    - Reemplaza solo el conjunto sintetico identificado con el prefijo EST-.
      No elimina incidencias reales con otros codigos.
    - Crea usuarios de prueba para reportantes, responsables, operadores y supervisores.
    - Incluye historial, comentarios, evidencias, acciones correctivas y revisiones.
*/

USE CAMPUSSEGURO360;
GO

SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF NOT EXISTS (SELECT 1 FROM ROL WHERE ID_ROL = 5)
       OR NOT EXISTS (SELECT 1 FROM CATEGORIA WHERE ID_CATEGORIA BETWEEN 1 AND 8)
       OR NOT EXISTS (SELECT 1 FROM ESTADO_INCIDENCIA WHERE ID_ESTADO BETWEEN 1 AND 9)
    BEGIN
        THROW 51000, 'Ejecute primero el script 05-datos-iniciales.sql.', 1;
    END;

    /* Compatibilidad con bases antiguas: algunas aun tienen el rango 1-3.
       El script no modifica permisos ni estructura; genera el maximo valido
       segun la restriccion existente. Para utilizar 1-4, ejecute antes
       03-migracion-prioridad-automatica.sql con un usuario con ALTER TABLE. */
    DECLARE @MaxReincidencia INT = 4;
    IF EXISTS (
        SELECT 1 FROM sys.check_constraints
        WHERE name = 'CK_INCIDENCIA_REINCIDENCIA'
          AND (definition LIKE '%BETWEEN (1) AND (3)%'
               OR definition LIKE '%BETWEEN 1 AND 3%')
    )
        SET @MaxReincidencia = 3;

    /* Permite regenerar el escenario estadistico sin tocar datos reales. */
    DELETE r FROM INCIDENCIA_REVISION_SUPERVISOR r
    INNER JOIN INCIDENCIA i ON i.ID_INCIDENCIA = r.ID_INCIDENCIA
    WHERE i.CODIGO LIKE 'EST-%';
    DELETE e FROM INCIDENCIA_EVIDENCIA e
    INNER JOIN INCIDENCIA i ON i.ID_INCIDENCIA = e.ID_INCIDENCIA
    WHERE i.CODIGO LIKE 'EST-%';
    DELETE a FROM INCIDENCIA_ACCION_CORRECTIVA a
    INNER JOIN INCIDENCIA i ON i.ID_INCIDENCIA = a.ID_INCIDENCIA
    WHERE i.CODIGO LIKE 'EST-%';
    DELETE c FROM INCIDENCIA_COMENTARIO c
    INNER JOIN INCIDENCIA i ON i.ID_INCIDENCIA = c.ID_INCIDENCIA
    WHERE i.CODIGO LIKE 'EST-%';
    DELETE h FROM INCIDENCIA_HISTORIAL h
    INNER JOIN INCIDENCIA i ON i.ID_INCIDENCIA = h.ID_INCIDENCIA
    WHERE i.CODIGO LIKE 'EST-%';
    DELETE FROM INCIDENCIA WHERE CODIGO LIKE 'EST-%';

    /* Password BCrypt compartido para los usuarios de prueba: 123456 */
    INSERT INTO USUARIO (USUARIO, NOMBRES, APE_PATERNO, APE_MATERNO, CORREO, PASSWORD, ESTADO, ACTIVO)
    SELECT v.USUARIO, v.NOMBRES, v.APE_PATERNO, v.APE_MATERNO, v.CORREO,
           '$2a$10$UPRZIxR6bNyBJdZTUZcYVe7M8syeIva9qGQQCvOYM5k4BVn9zPEre', 1, 1
    FROM (VALUES
        ('stats_operador_01', 'Ana', 'Torres', 'Rojas', 'stats.operador01@demo.com'),
        ('stats_operador_02', 'Luis', 'Mendoza', 'Paz', 'stats.operador02@demo.com'),
        ('stats_operador_03', 'Rosa', 'Vega', 'Diaz', 'stats.operador03@demo.com'),
        ('stats_supervisor_01', 'Carlos', 'Flores', 'Ruiz', 'stats.supervisor01@demo.com'),
        ('stats_supervisor_02', 'Maria', 'Salazar', 'Leon', 'stats.supervisor02@demo.com'),
        ('stats_supervisor_03', 'Jorge', 'Castro', 'Paredes', 'stats.supervisor03@demo.com'),
        ('stats_responsable_01', 'Diego', 'Ramos', 'Silva', 'stats.responsable01@demo.com'),
        ('stats_responsable_02', 'Lucia', 'Herrera', 'Soto', 'stats.responsable02@demo.com'),
        ('stats_responsable_03', 'Pedro', 'Navarro', 'Luna', 'stats.responsable03@demo.com'),
        ('stats_responsable_04', 'Elena', 'Campos', 'Vera', 'stats.responsable04@demo.com'),
        ('stats_responsable_05', 'Miguel', 'Aguilar', 'Rey', 'stats.responsable05@demo.com'),
        ('stats_responsable_06', 'Sonia', 'Mora', 'Gil', 'stats.responsable06@demo.com'),
        ('stats_responsable_07', 'Raul', 'Pineda', 'Rios', 'stats.responsable07@demo.com'),
        ('stats_responsable_08', 'Paola', 'Ibarra', 'Cruz', 'stats.responsable08@demo.com'),
        ('stats_responsable_09', 'Victor', 'Valdez', 'Nunez', 'stats.responsable09@demo.com'),
        ('stats_responsable_10', 'Andrea', 'Bravo', 'Mejia', 'stats.responsable10@demo.com'),
        ('stats_responsable_11', 'Hugo', 'Fuentes', 'Lopez', 'stats.responsable11@demo.com'),
        ('stats_responsable_12', 'Carla', 'Guerra', 'Mendez', 'stats.responsable12@demo.com'),
        ('stats_reportante_01', 'Jose', 'Garcia', 'Lopez', 'stats.reportante01@demo.com'),
        ('stats_reportante_02', 'Patricia', 'Ramirez', 'Sanchez', 'stats.reportante02@demo.com'),
        ('stats_reportante_03', 'Ricardo', 'Vargas', 'Quispe', 'stats.reportante03@demo.com'),
        ('stats_reportante_04', 'Diana', 'Mendez', 'Ramos', 'stats.reportante04@demo.com'),
        ('stats_reportante_05', 'Fernando', 'Cruz', 'Vega', 'stats.reportante05@demo.com'),
        ('stats_reportante_06', 'Claudia', 'Reyes', 'Soto', 'stats.reportante06@demo.com'),
        ('stats_reportante_07', 'Martin', 'Diaz', 'Torres', 'stats.reportante07@demo.com'),
        ('stats_reportante_08', 'Valeria', 'Rojas', 'Luna', 'stats.reportante08@demo.com'),
        ('stats_reportante_09', 'Oscar', 'Silva', 'Castro', 'stats.reportante09@demo.com'),
        ('stats_reportante_10', 'Natalia', 'Flores', 'Paz', 'stats.reportante10@demo.com'),
        ('stats_reportante_11', 'Alberto', 'Mora', 'Vera', 'stats.reportante11@demo.com'),
        ('stats_reportante_12', 'Monica', 'Campos', 'Ruiz', 'stats.reportante12@demo.com'),
        ('stats_reportante_13', 'Sergio', 'Luna', 'Rey', 'stats.reportante13@demo.com'),
        ('stats_reportante_14', 'Karen', 'Aguilar', 'Gil', 'stats.reportante14@demo.com'),
        ('stats_reportante_15', 'Ruben', 'Paredes', 'Rios', 'stats.reportante15@demo.com'),
        ('stats_reportante_16', 'Silvia', 'Bravo', 'Cruz', 'stats.reportante16@demo.com'),
        ('stats_reportante_17', 'Daniel', 'Fuentes', 'Nunez', 'stats.reportante17@demo.com'),
        ('stats_reportante_18', 'Teresa', 'Guerra', 'Mejia', 'stats.reportante18@demo.com'),
        ('stats_reportante_19', 'Ivan', 'Ibarra', 'Lopez', 'stats.reportante19@demo.com'),
        ('stats_reportante_20', 'Beatriz', 'Valdez', 'Sanchez', 'stats.reportante20@demo.com')
    ) v(USUARIO, NOMBRES, APE_PATERNO, APE_MATERNO, CORREO)
    WHERE NOT EXISTS (SELECT 1 FROM USUARIO u WHERE u.USUARIO = v.USUARIO OR u.CORREO = v.CORREO);

    INSERT INTO USUARIO_ROL (ID_USUARIO, ID_ROL, ESTADO, ACTIVO)
    SELECT u.ID_USUARIO,
           CASE
               WHEN u.USUARIO LIKE 'stats_operador[_]%' THEN 2
               WHEN u.USUARIO LIKE 'stats_responsable[_]%' THEN 3
               WHEN u.USUARIO LIKE 'stats_supervisor[_]%' THEN 4
               WHEN u.USUARIO LIKE 'stats_reportante[_]%' THEN 1
           END,
           1, 1
    FROM USUARIO u
    WHERE u.USUARIO LIKE 'stats[_]%'
      AND NOT EXISTS (
          SELECT 1 FROM USUARIO_ROL ur
          WHERE ur.ID_USUARIO = u.ID_USUARIO
            AND ur.ID_ROL = CASE
                WHEN u.USUARIO LIKE 'stats_operador[_]%' THEN 2
                WHEN u.USUARIO LIKE 'stats_responsable[_]%' THEN 3
                WHEN u.USUARIO LIKE 'stats_supervisor[_]%' THEN 4
                WHEN u.USUARIO LIKE 'stats_reportante[_]%' THEN 1
            END
      );

    DECLARE @Ahora DATETIME2 = SYSDATETIME();
    DECLARE @Operador INT = (SELECT TOP (1) ID_USUARIO FROM USUARIO WHERE USUARIO = 'stats_operador_01');
    DECLARE @Supervisor INT = (SELECT TOP (1) ID_USUARIO FROM USUARIO WHERE USUARIO = 'stats_supervisor_01');

    DECLARE @Reportantes TABLE (ORDEN INT PRIMARY KEY, ID_USUARIO INT NOT NULL);
    INSERT INTO @Reportantes (ORDEN, ID_USUARIO)
    SELECT ROW_NUMBER() OVER (ORDER BY ID_USUARIO), ID_USUARIO
    FROM USUARIO
    WHERE USUARIO LIKE 'stats_reportante[_]%';

    DECLARE @Responsables TABLE (ORDEN INT PRIMARY KEY, ID_USUARIO INT NOT NULL);
    INSERT INTO @Responsables (ORDEN, ID_USUARIO)
    SELECT ROW_NUMBER() OVER (ORDER BY ID_USUARIO), ID_USUARIO
    FROM USUARIO
    WHERE USUARIO LIKE 'stats_responsable[_]%';

    IF (SELECT COUNT(*) FROM @Reportantes) < 20 OR (SELECT COUNT(*) FROM @Responsables) < 12
        THROW 51001, 'No fue posible preparar los usuarios de datos estadisticos.', 1;

    DECLARE @Serie TABLE (N INT PRIMARY KEY);
    INSERT INTO @Serie (N)
    SELECT TOP (2000) ROW_NUMBER() OVER (ORDER BY (SELECT NULL))
    FROM sys.all_objects a CROSS JOIN sys.all_objects b;

    /* Distribucion de estados: 120 registrada, 180 clasificada, 200 asignada,
       360 en atencion, 120 en espera, 220 resuelta, 560 cerrada,
       120 cancelada y 120 reabierta. */
    INSERT INTO INCIDENCIA (
        CODIGO, ID_REPORTANTE, ID_RESPONSABLE, ID_CATEGORIA, ID_UBICACION,
        ID_SEVERIDAD, ID_PRIORIDAD, ID_ESTADO, ID_SLA, IMPACTO, URGENCIA,
        REINCIDENCIA, PUNTAJE_PRIORIDAD, TITULO, DESCRIPCION, FECHA_REGISTRO,
        FECHA_LIMITE, FECHA_RESOLUCION, FECHA_CIERRE, ACTIVO
    )
    SELECT
        CONCAT('EST-', RIGHT(CONCAT('0000', s.N), 4)),
        (SELECT ID_USUARIO FROM @Reportantes WHERE ORDEN = ((s.N - 1) % 20) + 1),
        CASE WHEN e.ID_ESTADO IN (1, 2) OR (e.ID_ESTADO = 8 AND s.N % 3 = 0) THEN NULL
             ELSE (SELECT ID_USUARIO FROM @Responsables WHERE ORDEN =
                 CASE
                     WHEN s.N % 10 IN (0, 1, 2) THEN 1
                     WHEN s.N % 10 IN (3, 4) THEN 2
                     WHEN s.N % 10 = 5 THEN 3
                     WHEN s.N % 10 = 6 THEN 4
                     WHEN s.N % 10 = 7 THEN 5
                     WHEN s.N % 10 = 8 THEN 6
                     ELSE 7 + (s.N % 6)
                 END) END,
        ((s.N - 1) % 8) + 1,
        ((s.N * 3 - 1) % 8) + 1,
        CASE WHEN e.ID_ESTADO = 1 THEN NULL ELSE sev.ID_SEVERIDAD END,
        CASE WHEN e.ID_ESTADO = 1 THEN NULL ELSE sev.ID_SEVERIDAD END,
        e.ID_ESTADO,
        CASE WHEN e.ID_ESTADO = 1 THEN NULL ELSE sev.ID_SEVERIDAD END,
        CASE WHEN e.ID_ESTADO = 1 THEN NULL ELSE ((s.N - 1) % 4) + 1 END,
        CASE WHEN e.ID_ESTADO = 1 THEN NULL ELSE ((s.N + 1) % 4) + 1 END,
        CASE WHEN e.ID_ESTADO = 1 THEN NULL ELSE ((s.N + 2) % @MaxReincidencia) + 1 END,
        CASE WHEN e.ID_ESTADO = 1 THEN NULL ELSE CAST(sev.ID_SEVERIDAD AS DECIMAL(4,2)) END,
        CASE ((s.N - 1) % 8)
            WHEN 0 THEN 'Equipo requiere revision preventiva'
            WHEN 1 THEN 'Aplicacion institucional presenta error'
            WHEN 2 THEN 'Intermitencia de conectividad de red'
            WHEN 3 THEN 'Solicitud de acceso a recurso institucional'
            WHEN 4 THEN 'Correo institucional no sincroniza'
            WHEN 5 THEN 'Impresora compartida no disponible'
            WHEN 6 THEN 'Alerta de seguridad requiere analisis'
            ELSE 'Incidencia operativa reportada por usuario'
        END,
        CONCAT(N'Registro generado para pruebas de estadisticas, SLA, carga de responsables y trazabilidad. Caso N. ', s.N),
        reg.FECHA_REGISTRO,
        limite.FECHA_LIMITE,
        CASE WHEN e.ID_ESTADO IN (6, 7) THEN
             CASE WHEN s.N % 4 = 0 THEN DATEADD(HOUR, 6, limite.FECHA_LIMITE)
                  ELSE DATEADD(HOUR, -2, limite.FECHA_LIMITE) END
             ELSE NULL END,
        CASE WHEN e.ID_ESTADO = 7 THEN
             CASE WHEN s.N % 4 = 0 THEN DATEADD(HOUR, 8, limite.FECHA_LIMITE)
                  ELSE DATEADD(HOUR, -1, limite.FECHA_LIMITE) END
             ELSE NULL END,
        1
    FROM @Serie s
    CROSS APPLY (VALUES (
        CASE
            WHEN s.N <= 120 THEN 1 WHEN s.N <= 300 THEN 2 WHEN s.N <= 500 THEN 3
            WHEN s.N <= 860 THEN 4 WHEN s.N <= 980 THEN 5 WHEN s.N <= 1200 THEN 6
            WHEN s.N <= 1760 THEN 7 WHEN s.N <= 1880 THEN 8 ELSE 9
        END
    )) e(ID_ESTADO)
    CROSS APPLY (VALUES (((s.N - 1) % 4) + 1)) sev(ID_SEVERIDAD)
    /* Multiplicador coprimo para distribuir los 2,000 casos en 540 dias
       sin concentrar el periodo reciente en determinados estados. */
    CROSS APPLY (VALUES (DATEADD(MINUTE, -((s.N * 9973) % 777600), @Ahora))) reg(FECHA_REGISTRO)
    CROSS APPLY (VALUES (
        CASE
            WHEN e.ID_ESTADO = 1 THEN NULL
            WHEN e.ID_ESTADO IN (3, 4, 5, 9) AND s.N % 6 = 0 THEN DATEADD(HOUR, -12, @Ahora)
            WHEN e.ID_ESTADO IN (3, 4, 5, 9) AND s.N % 6 = 1 THEN DATEADD(HOUR, 8, @Ahora)
            WHEN e.ID_ESTADO IN (3, 4, 5, 9) THEN DATEADD(DAY, 2 + (s.N % 4), @Ahora)
            WHEN sev.ID_SEVERIDAD = 1 THEN DATEADD(MINUTE, 4320, reg.FECHA_REGISTRO)
            WHEN sev.ID_SEVERIDAD = 2 THEN DATEADD(MINUTE, 1440, reg.FECHA_REGISTRO)
            WHEN sev.ID_SEVERIDAD = 3 THEN DATEADD(MINUTE, 480, reg.FECHA_REGISTRO)
            ELSE DATEADD(MINUTE, 120, reg.FECHA_REGISTRO)
        END
    )) limite(FECHA_LIMITE)
    WHERE NOT EXISTS (
        SELECT 1 FROM INCIDENCIA i WHERE i.CODIGO = CONCAT('EST-', RIGHT(CONCAT('0000', s.N), 4))
    );

    /* Historial que permite reconstruir el ciclo completo de cada incidencia. */
    INSERT INTO INCIDENCIA_HISTORIAL (ID_INCIDENCIA, ID_USUARIO, TIPO_EVENTO, VALOR_ANTERIOR, VALOR_NUEVO, DESCRIPCION, FECHA_EVENTO, ACTIVO)
    SELECT i.ID_INCIDENCIA, i.ID_REPORTANTE, 'REGISTRO_INCIDENCIA', NULL, 'Registrada',
           'Incidencia registrada por el reportante.', i.FECHA_REGISTRO, 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%'
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_HISTORIAL h WHERE h.ID_INCIDENCIA = i.ID_INCIDENCIA AND h.TIPO_EVENTO = 'REGISTRO_INCIDENCIA');

    INSERT INTO INCIDENCIA_HISTORIAL (ID_INCIDENCIA, ID_USUARIO, TIPO_EVENTO, VALOR_ANTERIOR, VALOR_NUEVO, DESCRIPCION, IMPACTO, URGENCIA, REINCIDENCIA, PUNTAJE_PRIORIDAD, FECHA_EVENTO, ACTIVO)
    SELECT i.ID_INCIDENCIA, @Operador, 'CLASIFICACION', NULL, CAST(i.PUNTAJE_PRIORIDAD AS VARCHAR(20)),
           'Incidencia clasificada, priorizada y con SLA asignado.', i.IMPACTO, i.URGENCIA, i.REINCIDENCIA, i.PUNTAJE_PRIORIDAD,
           DATEADD(MINUTE, 20, i.FECHA_REGISTRO), 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_ESTADO IN (2,3,4,5,6,7,9)
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_HISTORIAL h WHERE h.ID_INCIDENCIA = i.ID_INCIDENCIA AND h.TIPO_EVENTO = 'CLASIFICACION');

    INSERT INTO INCIDENCIA_HISTORIAL (ID_INCIDENCIA, ID_USUARIO, TIPO_EVENTO, VALOR_ANTERIOR, VALOR_NUEVO, DESCRIPCION, FECHA_EVENTO, ACTIVO)
    SELECT i.ID_INCIDENCIA, @Operador, 'ASIGNACION_RESPONSABLE', 'SIN_RESPONSABLE', CAST(i.ID_RESPONSABLE AS VARCHAR(20)),
           'Incidencia asignada al responsable de atencion.', DATEADD(MINUTE, 35, i.FECHA_REGISTRO), 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_ESTADO IN (3,4,5,6,7,9)
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_HISTORIAL h WHERE h.ID_INCIDENCIA = i.ID_INCIDENCIA AND h.TIPO_EVENTO = 'ASIGNACION_RESPONSABLE');

    INSERT INTO INCIDENCIA_HISTORIAL (ID_INCIDENCIA, ID_USUARIO, TIPO_EVENTO, VALOR_ANTERIOR, VALOR_NUEVO, DESCRIPCION, FECHA_EVENTO, ACTIVO)
    SELECT i.ID_INCIDENCIA, i.ID_RESPONSABLE, 'INICIO_ATENCION', 'Asignada', 'En atencion',
           'Responsable inicio la atencion de la incidencia.', DATEADD(MINUTE, 50, i.FECHA_REGISTRO), 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_ESTADO IN (4,5,6,7,9)
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_HISTORIAL h WHERE h.ID_INCIDENCIA = i.ID_INCIDENCIA AND h.TIPO_EVENTO = 'INICIO_ATENCION');

    INSERT INTO INCIDENCIA_HISTORIAL (ID_INCIDENCIA, ID_USUARIO, TIPO_EVENTO, VALOR_ANTERIOR, VALOR_NUEVO, DESCRIPCION, FECHA_EVENTO, ACTIVO)
    SELECT i.ID_INCIDENCIA, i.ID_RESPONSABLE, 'EN_ESPERA', 'En atencion', 'En espera',
           'Atencion en espera por dependencia externa.', DATEADD(HOUR, 2, i.FECHA_REGISTRO), 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_ESTADO = 5
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_HISTORIAL h WHERE h.ID_INCIDENCIA = i.ID_INCIDENCIA AND h.TIPO_EVENTO = 'EN_ESPERA');

    INSERT INTO INCIDENCIA_HISTORIAL (ID_INCIDENCIA, ID_USUARIO, TIPO_EVENTO, VALOR_ANTERIOR, VALOR_NUEVO, DESCRIPCION, FECHA_EVENTO, ACTIVO)
    SELECT i.ID_INCIDENCIA, i.ID_RESPONSABLE, 'RESOLUCION', 'En atencion', 'Resuelta',
           'Responsable registro la solucion aplicada.', i.FECHA_RESOLUCION, 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_ESTADO IN (6,7)
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_HISTORIAL h WHERE h.ID_INCIDENCIA = i.ID_INCIDENCIA AND h.TIPO_EVENTO = 'RESOLUCION');

    INSERT INTO INCIDENCIA_HISTORIAL (ID_INCIDENCIA, ID_USUARIO, TIPO_EVENTO, VALOR_ANTERIOR, VALOR_NUEVO, DESCRIPCION, FECHA_EVENTO, ACTIVO)
    SELECT i.ID_INCIDENCIA, @Supervisor, 'CIERRE', 'Resuelta', 'Cerrada',
           'Cierre definitivo de incidencia.', i.FECHA_CIERRE, 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_ESTADO = 7
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_HISTORIAL h WHERE h.ID_INCIDENCIA = i.ID_INCIDENCIA AND h.TIPO_EVENTO = 'CIERRE');

    INSERT INTO INCIDENCIA_HISTORIAL (ID_INCIDENCIA, ID_USUARIO, TIPO_EVENTO, VALOR_ANTERIOR, VALOR_NUEVO, DESCRIPCION, FECHA_EVENTO, ACTIVO)
    SELECT i.ID_INCIDENCIA, @Operador, 'CANCELACION', 'Registrada o clasificada', 'Cancelada',
           'Incidencia cancelada por duplicidad o falta de informacion.', DATEADD(HOUR, 1, i.FECHA_REGISTRO), 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_ESTADO = 8
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_HISTORIAL h WHERE h.ID_INCIDENCIA = i.ID_INCIDENCIA AND h.TIPO_EVENTO = 'CANCELACION');

    INSERT INTO INCIDENCIA_HISTORIAL (ID_INCIDENCIA, ID_USUARIO, TIPO_EVENTO, VALOR_ANTERIOR, VALOR_NUEVO, DESCRIPCION, FECHA_EVENTO, ACTIVO)
    SELECT i.ID_INCIDENCIA, @Supervisor, 'REAPERTURA', 'Resuelta', 'Reabierta',
           'Supervisor reabrio la incidencia para una nueva atencion.', DATEADD(HOUR, 3, i.FECHA_REGISTRO), 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_ESTADO = 9
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_HISTORIAL h WHERE h.ID_INCIDENCIA = i.ID_INCIDENCIA AND h.TIPO_EVENTO = 'REAPERTURA');

    /* Colaboracion: se distribuye entre incidencias para poblar la trazabilidad. */
    INSERT INTO INCIDENCIA_COMENTARIO (ID_INCIDENCIA, ID_USUARIO, COMENTARIO, FECHA_REGISTRO, ACTIVO)
    SELECT i.ID_INCIDENCIA, COALESCE(i.ID_RESPONSABLE, i.ID_REPORTANTE),
           N'Comentario de seguimiento generado para validar la colaboracion y trazabilidad.', DATEADD(HOUR, 1, i.FECHA_REGISTRO), 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_INCIDENCIA % 3 = 0
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_COMENTARIO c WHERE c.ID_INCIDENCIA = i.ID_INCIDENCIA AND c.COMENTARIO LIKE N'Comentario de seguimiento generado%');

    INSERT INTO INCIDENCIA_EVIDENCIA (ID_INCIDENCIA, ID_USUARIO, NOMBRE_ARCHIVO, RUTA_ARCHIVO, DESCRIPCION, FECHA_REGISTRO, ACTIVO)
    SELECT i.ID_INCIDENCIA, COALESCE(i.ID_RESPONSABLE, i.ID_REPORTANTE),
           CONCAT('evidencia-est-', i.ID_INCIDENCIA, '.pdf'), CONCAT('/evidencias/estadisticas/evidencia-est-', i.ID_INCIDENCIA, '.pdf'),
           'Evidencia descriptiva generada para pruebas.', DATEADD(HOUR, 2, i.FECHA_REGISTRO), 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_INCIDENCIA % 4 = 0
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_EVIDENCIA e WHERE e.ID_INCIDENCIA = i.ID_INCIDENCIA AND e.NOMBRE_ARCHIVO = CONCAT('evidencia-est-', i.ID_INCIDENCIA, '.pdf'));

    INSERT INTO INCIDENCIA_ACCION_CORRECTIVA (ID_INCIDENCIA, ID_USUARIO, DESCRIPCION, FECHA_REGISTRO, ACTIVO)
    SELECT i.ID_INCIDENCIA, i.ID_RESPONSABLE, N'Accion correctiva registrada para resolver la causa identificada.', DATEADD(HOUR, 3, i.FECHA_REGISTRO), 1
    FROM INCIDENCIA i WHERE i.CODIGO LIKE 'EST-%' AND i.ID_RESPONSABLE IS NOT NULL AND i.ID_INCIDENCIA % 5 = 0
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_ACCION_CORRECTIVA a WHERE a.ID_INCIDENCIA = i.ID_INCIDENCIA AND a.DESCRIPCION LIKE N'Accion correctiva registrada%');

    /* Criticas cerradas aprobadas y reabiertas con revision rechazada. */
    INSERT INTO INCIDENCIA_REVISION_SUPERVISOR (ID_INCIDENCIA, ID_SUPERVISOR, APROBADA, OBSERVACION, FECHA_REVISION, ACTIVO)
    SELECT i.ID_INCIDENCIA, @Supervisor, CASE WHEN i.ID_ESTADO = 7 THEN 1 ELSE 0 END,
           CASE WHEN i.ID_ESTADO = 7 THEN N'Revision aprobada: la solucion cumple los criterios de cierre.' ELSE N'Revision rechazada: requiere una nueva atencion.' END,
           DATEADD(MINUTE, 30, COALESCE(i.FECHA_RESOLUCION, i.FECHA_REGISTRO)), 1
    FROM INCIDENCIA i
    WHERE i.CODIGO LIKE 'EST-%' AND i.ID_SEVERIDAD = 4 AND i.ID_ESTADO IN (7,9)
      AND NOT EXISTS (SELECT 1 FROM INCIDENCIA_REVISION_SUPERVISOR r WHERE r.ID_INCIDENCIA = i.ID_INCIDENCIA);

    COMMIT TRANSACTION;

    SELECT
        COUNT(*) AS incidenciasEstadisticas,
        SUM(CASE WHEN ID_ESTADO = 7 THEN 1 ELSE 0 END) AS cerradas,
        SUM(CASE WHEN ID_ESTADO = 4 THEN 1 ELSE 0 END) AS enAtencion,
        SUM(CASE WHEN ID_ESTADO = 6 THEN 1 ELSE 0 END) AS resueltas,
        SUM(CASE WHEN ID_ESTADO = 8 THEN 1 ELSE 0 END) AS canceladas
    FROM INCIDENCIA
    WHERE CODIGO LIKE 'EST-%';
END TRY
BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
GO
