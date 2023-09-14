create table calendarios_notificaciones
(
    id integer,
    primary key (id)
)

create table comunidades
(
    id     integer,
    nombre varchar(255),
    primary key (id)
)

create table entidades
(
    id     integer,
    nombre varchar(255),
    tipo   varchar(255),
    primary key (id)
)

create table entidades_controladas_por_organismos_de_control
(
    organismo_id integer,
    entidad_id   integer,
    primary key (organismo_id, entidad_id)
)

alter table entidades_controladas_por_organismos_de_control
    add constraint FK7bde26xtuo638xi7bvjw44xth
        foreign key (entidad_id)
            references entidades
alter table entidades_controladas_por_organismos_de_control
    add constraint FKo0p1ftb30kjhj77d5fwl717qn
        foreign key (organismo_id)
            references organismos_de_control

create table establecimientos
(
    id         integer,
    nombre     varchar(255),
    latitud double,
    longitud double,
    entidad_id integer,
    primary key (id)
)

alter table establecimientos
    add constraint FKr86rbwx8ceodsoxmi9rnllr8e
        foreign key (entidad_id)
            references entidades

create table horarios_notificaciones
(
    calendario_id integer,
    fin           time,
    inicio        time,
    dia_de_semana varchar(255),
    primary key (calendario_id, dia_de_semana)
)

alter table horarios_notificaciones
    add constraint FKmo5hy242betklaj2s5m6cygmo
        foreign key (calendario_id)
            references calendarios_notificaciones

create table incidentes
(
    id               integer,
    fecha            timestamp,
    fecha_resolucion timestamp,
    observaciones    varchar(255),
    resuelto         boolean,
    reportante_id    integer,
    servicio_id      integer,
    entidad_id       integer,
    primary key (id)
)

alter table incidentes
    add constraint FKtmtn4400uft6uq13474qujauo
        foreign key (reportante_id)
            references usuarios
alter table incidentes
    add constraint FKmd6ft14bw4bgul84gk1ute3x4
        foreign key (servicio_id)
            references servicios
alter table incidentes
    add constraint FKk5gkoop0oh7i0nix9a62w0vd8
        foreign key (entidad_id)
            references entidades

create table incidentes_por_comunidades
(
    comunidad_id integer,
    incidente_id integer
)

alter table incidentes_por_comunidades
    add constraint FK1e0oif2dbuad6eaewdf30ycbe
        foreign key (incidente_id)
            references incidentes
alter table incidentes_por_comunidades
    add constraint FKrwdrrq41n0akq80gqk2hrupfn
        foreign key (comunidad_id)
            references comunidades

create table medios_de_comunicacion
(
    descripcion varchar(255),
    id          integer,
    primary key (id)
)

create table miembros_por_comunidades
(
    comunidad_id integer,
    usuario_id   integer
)

alter table miembros_por_comunidades
    add constraint FKoxpsno0moxgga807dmqm2857w
        foreign key (usuario_id)
            references usuarios
alter table miembros_por_comunidades
    add constraint FKhg18tu97e0k2nv71evjkx23qy
        foreign key (comunidad_id)
            references comunidades


create table notificaciones
(
    tipo        varchar(31),
    id          integer,
    fecha       timestamp,
    fecha_envio timestamp,
    receptor_id integer,
    primary key (id)
)

alter table notificaciones
    add constraint FK85l9q005gqdurek2lnal2c7rs
        foreign key (receptor_id)
            references usuarios

create table organismos_de_control
(
    id                 integer,
    correo_electronico varchar(255),
    nombre             varchar(255),
    responsable_id     integer,
    primary key (id)
)

alter table organismos_de_control
    add constraint FKjsxxeuk0nsb3gkvilr20grhro
        foreign key (responsable_id)
            references usuarios

create table servicios
(
    id                 integer,
    descripcion        varchar(255),
    tipo               varchar(255),
    establecimiento_id integer,
    primary key (id)
)

alter table servicios
    add constraint FKo04hkoh4n31m4ricjdfr6dhmd
        foreign key (establecimiento_id)
            references establecimientos

create table servicios_interesados_por_comunidades
(
    comunidad_id integer,
    servicio_id  integer
)

alter table servicios_interesados_por_comunidades
    add constraint FKaxxtwf6u5ks7j8f6smvvbbpge
        foreign key (servicio_id)
            references servicios
alter table servicios_interesados_por_comunidades
    add constraint FKst8bp7a6ptwul6oysmvq9ggg9
        foreign key (comunidad_id)
            references comunidades

create table usuarios
(
    id                           integer,
    apellido                     varchar(255),
    contrasenia                  varchar(255),
    correo_electronico           varchar(255),
    nombre                       varchar(255),
    usuario                      varchar(255),
    calendario_notificaciones_id integer,
    medio_de_comunicacion_id     integer,
    primary key (id)
)

alter table usuarios
    add constraint FK8j2k98ff70b6yld1yhpyakx5n
        foreign key (calendario_notificaciones_id)
            references calendarios_notificaciones
alter table usuarios
    add constraint FKnup5diq8gjsla0nsp9bmld8yx
        foreign key (medio_de_comunicacion_id)
            references medios_de_comunicacion

create table usuarios_interesados_por_entidades
(
    entidad_id integer,
    usuario_id integer
)

alter table usuarios_interesados_por_entidades
    add constraint FKtnjm9ud67v2c7cupqx4aoh83f
        foreign key (usuario_id)
            references usuarios
alter table usuarios_interesados_por_entidades
    add constraint FK5wwqyty4uvgl5sn8o6iyyh8iv
        foreign key (entidad_id)
            references entidades