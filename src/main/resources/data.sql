INSERT INTO rol (id_rol, nombre) VALUES
(1, 'ADMINISTRADOR'),
(2, 'OPERADOR_MESA_CONTROL'),
(3, 'RESPONSABLE_ATENCION'),
(4, 'REPORTANTE'),
(5, 'SUPERVISOR');

INSERT INTO usuario (id_usuario, usuario, nombres, apellidos, correo, password, estado) VALUES
(1, 'admin', 'Admin', 'Sistema', 'admin@demo.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', TRUE),
(2, 'operador', 'Operador', 'Mesa Control', 'operador@demo.com', '1725165c9a0b3698a3d01016e0d8205155820b8d7f21835ca64c0f81c728d880', TRUE),
(3, 'responsable', 'Responsable', 'Atencion', 'responsable@demo.com', '5287ed0b861b2537eeefd7c6d7572a82aafb2a9ba7580687d44ee7a1a3064c02', TRUE),
(4, 'reportante', 'Usuario', 'Reportante', 'reportante@demo.com', 'ec58aa2b7e8e365e4b3a4ca68ff64c67bf3fa083d57cce124104b6eceb640e1e', TRUE),
(5, 'supervisor', 'Supervisor', 'Incidencias', 'supervisor@demo.com', '02423ab2e61297b8262449c93e19be42fb5bbb275860a7d93b1ebdc7b6535ed7', TRUE);

INSERT INTO usuario_rol (id_usuario_rol, id_usuario, id_rol, estado) VALUES
(1, 1, 1, TRUE),
(2, 2, 2, TRUE),
(3, 3, 3, TRUE),
(4, 4, 4, TRUE),
(5, 5, 5, TRUE);

INSERT INTO categoria (id_categoria, nombre, descripcion) VALUES
(1, 'HARDWARE', 'Incidencias relacionadas con equipos fisicos, perifericos o componentes internos.'),
(2, 'SOFTWARE', 'Problemas con sistemas operativos, aplicaciones o herramientas instaladas.'),
(3, 'REDES', 'Fallas de conectividad, internet, VPN, cableado o equipos de red.'),
(4, 'ACCESOS', 'Solicitudes o incidencias relacionadas con usuarios, claves y permisos.'),
(5, 'CORREO', 'Problemas de correo electronico, buzones, envio o recepcion de mensajes.'),
(6, 'IMPRESION', 'Fallas en impresoras, colas de impresion o escaneo.'),
(7, 'SEGURIDAD', 'Eventos relacionados con seguridad informatica o alertas de riesgo.'),
(8, 'OTROS', 'Incidencias que no corresponden a una categoria especifica.');

INSERT INTO ubicacion (id_ubicacion, nombre, descripcion) VALUES
(1, 'SEDE PRINCIPAL', 'Oficina principal de la organizacion.'),
(2, 'AREA ADMINISTRATIVA', 'Zona de oficinas administrativas.'),
(3, 'AREA ACADEMICA', 'Ambientes academicos, aulas y laboratorios.'),
(4, 'MESA DE AYUDA', 'Area de soporte y atencion de incidencias.'),
(5, 'CENTRO DE DATOS', 'Sala de servidores, comunicaciones y equipamiento critico.'),
(6, 'ALMACEN', 'Ambiente de almacenamiento de equipos y materiales.'),
(7, 'SALA DE REUNIONES', 'Salas destinadas a reuniones y videoconferencias.'),
(8, 'REMOTO', 'Usuario o servicio atendido de forma remota.');

INSERT INTO severidad (id_severidad, nombre, nivel) VALUES
(1, 'BAJA', 1),
(2, 'MEDIA', 2),
(3, 'ALTA', 3),
(4, 'CRITICA', 4);

INSERT INTO prioridad (id_prioridad, nombre, nivel, puntaje_minimo, puntaje_maximo) VALUES
(1, 'BAJA', 1, 2, 3),
(2, 'MEDIA', 2, 4, 5),
(3, 'ALTA', 3, 6, 6),
(4, 'CRITICA', 4, 7, 7);

INSERT INTO estado_incidencia (id_estado, nombre) VALUES
(1, 'REGISTRADA'),
(2, 'CLASIFICADA'),
(3, 'ASIGNADA'),
(4, 'EN_ATENCION'),
(5, 'EN_ESPERA'),
(6, 'RESUELTA'),
(7, 'CERRADA'),
(8, 'CANCELADA'),
(9, 'REABIERTA');

INSERT INTO sla (id_sla, id_severidad, minutos_resolucion) VALUES
(1, 1, 4320),
(2, 2, 1440),
(3, 3, 480),
(4, 4, 120);
