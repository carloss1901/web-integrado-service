INSERT INTO rol (id_rol, nombre, activo) VALUES
(1, 'ADMINISTRADOR', 1),
(2, 'OPERADOR_MESA_CONTROL', 1),
(3, 'RESPONSABLE_ATENCION', 1),
(4, 'REPORTANTE', 1),
(5, 'SUPERVISOR', 1);

INSERT INTO usuario (id_usuario, usuario, nombres, apellidos, correo, password, estado, activo) VALUES
(1, 'admin', 'Admin', 'Sistema', 'admin@demo.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 1, 1),
(2, 'operador', 'Operador', 'Mesa Control', 'operador@demo.com', '1725165c9a0b3698a3d01016e0d8205155820b8d7f21835ca64c0f81c728d880', 1, 1),
(3, 'responsable', 'Responsable', 'Atencion', 'responsable@demo.com', '5287ed0b861b2537eeefd7c6d7572a82aafb2a9ba7580687d44ee7a1a3064c02', 1, 1),
(4, 'reportante', 'Usuario', 'Reportante', 'reportante@demo.com', 'ec58aa2b7e8e365e4b3a4ca68ff64c67bf3fa083d57cce124104b6eceb640e1e', 1, 1),
(5, 'supervisor', 'Supervisor', 'Incidencias', 'supervisor@demo.com', '02423ab2e61297b8262449c93e19be42fb5bbb275860a7d93b1ebdc7b6535ed7', 1, 1);

INSERT INTO usuario_rol (id_usuario_rol, id_usuario, id_rol, estado, activo) VALUES
(1, 1, 1, 1, 1),
(2, 2, 2, 1, 1),
(3, 3, 3, 1, 1),
(4, 4, 4, 1, 1),
(5, 5, 5, 1, 1);

INSERT INTO categoria (id_categoria, nombre, descripcion, activo) VALUES
(1, 'HARDWARE', 'Incidencias relacionadas con equipos fisicos, perifericos o componentes internos.', 1),
(2, 'SOFTWARE', 'Problemas con sistemas operativos, aplicaciones o herramientas instaladas.', 1),
(3, 'REDES', 'Fallas de conectividad, internet, VPN, cableado o equipos de red.', 1),
(4, 'ACCESOS', 'Solicitudes o incidencias relacionadas con usuarios, claves y permisos.', 1),
(5, 'CORREO', 'Problemas de correo electronico, buzones, envio o recepcion de mensajes.', 1),
(6, 'IMPRESION', 'Fallas en impresoras, colas de impresion o escaneo.', 1),
(7, 'SEGURIDAD', 'Eventos relacionados con seguridad informatica o alertas de riesgo.', 1),
(8, 'OTROS', 'Incidencias que no corresponden a una categoria especifica.', 1);

INSERT INTO ubicacion (id_ubicacion, nombre, descripcion, activo) VALUES
(1, 'SEDE PRINCIPAL', 'Oficina principal de la organizacion.', 1),
(2, 'AREA ADMINISTRATIVA', 'Zona de oficinas administrativas.', 1),
(3, 'AREA ACADEMICA', 'Ambientes academicos, aulas y laboratorios.', 1),
(4, 'MESA DE AYUDA', 'Area de soporte y atencion de incidencias.', 1),
(5, 'CENTRO DE DATOS', 'Sala de servidores, comunicaciones y equipamiento critico.', 1),
(6, 'ALMACEN', 'Ambiente de almacenamiento de equipos y materiales.', 1),
(7, 'SALA DE REUNIONES', 'Salas destinadas a reuniones y videoconferencias.', 1),
(8, 'REMOTO', 'Usuario o servicio atendido de forma remota.', 1);

INSERT INTO severidad (id_severidad, nombre, nivel, activo) VALUES
(1, 'BAJA', 1, 1),
(2, 'MEDIA', 2, 1),
(3, 'ALTA', 3, 1),
(4, 'CRITICA', 4, 1);

INSERT INTO prioridad (id_prioridad, nombre, nivel, puntaje_minimo, puntaje_maximo, activo) VALUES
(1, 'BAJA', 1, 2, 3, 1),
(2, 'MEDIA', 2, 4, 5, 1),
(3, 'ALTA', 3, 6, 6, 1),
(4, 'CRITICA', 4, 7, 7, 1);

INSERT INTO estado_incidencia (id_estado, nombre, activo) VALUES
(1, 'REGISTRADA', 1),
(2, 'CLASIFICADA', 1),
(3, 'ASIGNADA', 1),
(4, 'EN_ATENCION', 1),
(5, 'EN_ESPERA', 1),
(6, 'RESUELTA', 1),
(7, 'CERRADA', 1),
(8, 'CANCELADA', 1),
(9, 'REABIERTA', 1);

INSERT INTO sla (id_sla, id_severidad, minutos_resolucion, activo) VALUES
(1, 1, 4320, 1),
(2, 2, 1440, 1),
(3, 3, 480, 1),
(4, 4, 120, 1);
