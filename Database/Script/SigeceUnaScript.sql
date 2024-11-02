    CREATE SEQUENCE sis_usuarios_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_roles_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_mensajes_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_chats_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_notificacion_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_correos_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_parametros_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_sistemas_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_variables_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_variables_condicionales_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_variables_multimedia_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_gestion_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_areas_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_actividades_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_subactividades_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_seguimiento_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;

    CREATE SEQUENCE sis_aprobaciones_seq01 
        INCREMENT BY 1 
        START WITH 1 
        NOMAXVALUE 
        NOMINVALUE 
        NOCACHE
    ;


    CREATE TABLE
        sis_sistemas (
            sis_id NUMBER NOT NULL,
            sis_nombre VARCHAR2 (200) NOT NULL,
            sis_version NUMBER DEFAULT 1 NOT NULL
        );

    CREATE UNIQUE INDEX sis_sistemas_ind01 ON sis_sistemas (sis_nombre);

    ALTER TABLE sis_sistemas ADD CONSTRAINT sis_sistemas_pk PRIMARY KEY (sis_id);

    CREATE TABLE
        sis_roles (
            rol_id NUMBER NOT NULL,
            rol_nombre VARCHAR2 (300) NOT NULL,
            rol_sis_id NUMBER NOT NULL,
            rol_version NUMBER DEFAULT 1 NOT NULL
        );

    CREATE UNIQUE INDEX sis_roles_ind01 ON sis_roles (rol_nombre);

    ALTER TABLE sis_roles ADD CONSTRAINT sis_roles_pk PRIMARY KEY (rol_id);

    CREATE TABLE
        sis_usuarios (
            user_id NUMBER NOT NULL,
            user_nombre VARCHAR2 (300) NOT NULL,
            user_apellidos VARCHAR2 (300) NOT NULL,
            user_cedula VARCHAR2 (100) NOT NULL,
            user_correo VARCHAR2 (200) NOT NULL,
            user_telefono VARCHAR2 (300) NOT NULL,
            user_celular VARCHAR2 (300) NOT NULL,
            user_idioma VARCHAR2 (100) NOT NULL,
            user_foto BLOB NOT NULL,
            user_usuario VARCHAR2 (100) NOT NULL,
            user_clave VARCHAR2 (100) NOT NULL,
            user_estado VARCHAR2 (1) DEFAULT 'I' NOT NULL CONSTRAINT sis_usuarios_ck01 CHECK (user_estado in ('A', 'I')),
            user_status VARCHAR2 (200) DEFAULT 'En Linea' NOT NULL,
            user_ar_id NUMBER ,
            user_version NUMBER DEFAULT 1 NOT NULL
        );

    CREATE UNIQUE INDEX sis_usuarios_ind01 ON sis_usuarios (user_usuario);

    CREATE UNIQUE INDEX sis_usuarios_ind02 ON sis_usuarios (user_correo);

    CREATE UNIQUE INDEX sis_usuarios_ind03 ON sis_usuarios (user_cedula);

    ALTER TABLE sis_usuarios ADD CONSTRAINT sis_usuarios_pk PRIMARY KEY (user_id);

    CREATE TABLE
        sis_roles_usuarios (
            sru_rol_id NUMBER NOT NULL,
            sru_user_id NUMBER NOT NULL
        );

    CREATE TABLE
        sis_mensajes (
            sms_id NUMBER NOT NULL,
            sms_texto CLOB NOT NULL,
            sms_archivo BLOB,
            sms_tiempo DATE NOT NULL,
            sms_usu_id_emisor NUMBER NOT NULL,
            sms_chat_id NUMBER NOT NULL,
            sms_version NUMBER DEFAULT 1 NOT NULL
        );

    ALTER TABLE sis_mensajes ADD CONSTRAINT sis_mensajes_pk PRIMARY KEY (sms_id);

    CREATE TABLE
        sis_chats (
            cht_id NUMBER NOT NULL,
            cht_fecha DATE NOT NULL,
            cht_emisor_id NUMBER NOT NULL,
            cht_receptor_id NUMBER NOT NULL,
            cht_version NUMBER DEFAULT 1 NOT NULL
        );

    ALTER TABLE sis_chats ADD CONSTRAINT sis_chats_pk PRIMARY KEY (cht_id);

    CREATE TABLE
        sis_parametros (
            par_id NUMBER NOT NULL,
            par_correo VARCHAR2 (300) NOT NULL,
            par_clave VARCHAR2 (300) NOT NULL,
            par_puerto NUMBER NOT NULL,
            par_server VARCHAR2 (300) NOT NULL,
            par_protocolo VARCHAR2 (300) NOT NULL,
            par_timeout NUMBER NOT NULL,
            par_version NUMBER DEFAULT 1 NOT NULL
        );

    ALTER TABLE sis_parametros ADD CONSTRAINT sis_parametros_pk PRIMARY KEY (par_id);

    CREATE TABLE
        sis_notificacion (
            not_id NUMBER NOT NULL,
            not_nombre VARCHAR2 (300) NOT NULL,
            not_plantilla CLOB NOT NULL,
            not_version NUMBER DEFAULT 1 NOT NULL
        );

    CREATE UNIQUE INDEX sis_notificacion_ind01 ON sis_notificacion (not_nombre);

    ALTER TABLE sis_notificacion ADD CONSTRAINT sis_notificacion_pk PRIMARY KEY (not_id);

    CREATE TABLE
        sis_variables (
            var_id NUMBER NOT NULL,
            var_nombre VARCHAR2 (300) NOT NULL,
            tipo VARCHAR2 (300) NOT NULL,
            var_valor CLOB,
            var_valor_multimedia BLOB,
            var_not_id NUMBER NOT NULL,
            var_version NUMBER DEFAULT 1 NOT NULL
        );

    ALTER TABLE sis_variables ADD CONSTRAINT sis_variables_pk PRIMARY KEY (var_id);

    CREATE TABLE
        sis_variables_condicionales (
            vcon_id NUMBER NOT NULL,
            vcond_valor VARCHAR2 (300) NOT NULL,
            vcond_resultado CLOB,
            vcon_var_id NUMBER NOT NULL,
            vcon_version NUMBER DEFAULT 1 NOT NULL
        );

    ALTER TABLE sis_variables_condicionales ADD CONSTRAINT sis_variables_condicionales_pk PRIMARY KEY (vcon_id);

    CREATE TABLE
        sis_variables_multimedia (
            media_id NUMBER NOT NULL,
            media_url BLOB NOT NULL,
            media_tipo VARCHAR2 (300) NOT NULL CONSTRAINT sis_variables_multimedia_ck01 CHECK (media_tipo in ('Imagen', 'Video')),
            media_var_id NUMBER NOT NULL,
            media_version NUMBER DEFAULT 1 NOT NULL
        );

    ALTER TABLE sis_variables_multimedia ADD CONSTRAINT sis_variables_multimedia_pk PRIMARY KEY (media_id);

CREATE TABLE
    sis_correos (
        cor_id NUMBER NOT NULL,
        cor_asunto VARCHAR2 (300) NOT NULL,
        cor_destinatario VARCHAR2 (300) NOT NULL,
        cor_resultado CLOB NOT NULL,
        cor_estado VARCHAR2 (1) DEFAULT 'P' NOT NULL CONSTRAINT sis_correos_ck01 CHECK (cor_estado in ('E', 'P')),
        cor_fecha DATE NOT NULL,
        cor_not_id NUMBER NOT NULL,
        cor_version NUMBER DEFAULT 1 NOT NULL,
        cor_adjuntos BLOB,
        COR_CONTENT_IDS BLOB
    );

    ALTER TABLE sis_correos ADD CONSTRAINT sis_correos_pk PRIMARY KEY (cor_id);

    CREATE TABLE
        sis_gestion (
            ges_id NUMBER NOT NULL,
            ges_fecha_creacion DATE NOT NULL,
            ges_fecha_solucion DATE NOT NULL,
            ges_asunto VARCHAR2 (500) NOT NULL,
            ges_descripcion VARCHAR2 (1000) NOT NULL,
            ges_estado VARCHAR2 (1) DEFAULT 'E' NOT NULL CONSTRAINT sis_gestion_ck01 CHECK (ges_estado in ('A', 'R', 'C', 'E', 'S')),
            ges_solicitante_id NUMBER NOT NULL,
            ges_asignado_id NUMBER NOT NULL,
            ges_sub_id NUMBER ,
            ges_act_id NUMBER ,
            ges_archivo CLOB NOT NULL,
            ges_version NUMBER DEFAULT 1 NOT NULL
        );

    ALTER TABLE sis_gestion ADD CONSTRAINT sis_gestion_pk PRIMARY KEY (ges_id);

    CREATE TABLE
        sis_areas (
            ar_id NUMBER NOT NULL,
            ar_nombre VARCHAR2 (300) NOT NULL,
            ar_estado VARCHAR2 (1) DEFAULT 'A' NOT NULL CONSTRAINT sis_areas_ck01 CHECK (ar_estado in ('A', 'I')),
            ar_version NUMBER DEFAULT 1 NOT NULL
        );

    CREATE UNIQUE INDEX sis_areas_ind01 ON sis_areas (ar_nombre);

    ALTER TABLE sis_areas ADD CONSTRAINT sis_areas_pk PRIMARY KEY (ar_id);

    CREATE TABLE
        sis_actividades (
            act_id NUMBER NOT NULL,
            act_nombre VARCHAR2 (300) NOT NULL,
            act_ar_id NUMBER NOT NULL,
            act_version NUMBER DEFAULT 1 NOT NULL
        );

    CREATE UNIQUE INDEX sis_actividades_ind01 ON sis_actividades (act_nombre);

    ALTER TABLE sis_actividades ADD CONSTRAINT sis_actividades_pk PRIMARY KEY (act_id);

    CREATE TABLE
        sis_subactividades (
            sub_id NUMBER NOT NULL,
            sub_nombre VARCHAR2 (300) NOT NULL,
            sub_act_id NUMBER NOT NULL,
            sub_version NUMBER DEFAULT 1 NOT NULL
        );

    CREATE UNIQUE INDEX sis_subactividades_ind01 ON sis_subactividades (sub_nombre);

    ALTER TABLE sis_subactividades ADD CONSTRAINT sis_subactividades_pk PRIMARY KEY (sub_id);

    CREATE TABLE
        sis_seguimiento (
            seg_id NUMBER NOT NULL,
            seg_fecha DATE NOT NULL,
            seg_descripcion VARCHAR2 (1000) NOT NULL,
            seg_archivo CLOB,
            seg_estado VARCHAR2 (1) DEFAULT 'S' NOT NULL CONSTRAINT sis_seguimiento_ck01 CHECK (seg_estado in ('A', 'R', 'S')),
            seg_user_id NUMBER NOT NULL,
            seg_ges_id NUMBER NOT NULL,
            seg_version NUMBER DEFAULT 1 NOT NULL
        );

    ALTER TABLE sis_seguimiento ADD CONSTRAINT sis_seguimiento_pk PRIMARY KEY (seg_id);

    CREATE TABLE
        sis_aprobaciones (
            apro_id NUMBER NOT NULL,
            apro_estado VARCHAR2 (1) DEFAULT 'P' NOT NULL CONSTRAINT sis_aprobaciones_ck01 CHECK (apro_estado in ('A', 'R')),
            apro_descripcion VARCHAR2 (1000) NOT NULL,
            apro_comentario VARCHAR2 (1000),
            apro_solucion VARCHAR2 (3000),
            apro_fecha DATE NOT NULL,
            apro_archivo CLOB,
            apro_user_id NUMBER NOT NULL,
            apro_ges_id NUMBER NOT NULL,
            apro_version NUMBER DEFAULT 1 NOT NULL
        );

    ALTER TABLE sis_aprobaciones ADD CONSTRAINT sis_aprobaciones_pk PRIMARY KEY (apro_id);

    CREATE TABLE 
        sis_aprobadores (
            sgu_user_id NUMBER NOT NULL,
            sgu_ges_id NUMBER NOT NULL
        );


    --Foreign Keys
    ALTER TABLE sis_roles ADD CONSTRAINT sis_roles_fk01 FOREIGN KEY (rol_sis_id) REFERENCES sis_sistemas (sis_id);

    ALTER TABLE sis_usuarios ADD CONSTRAINT sis_usuarios_fk01 FOREIGN KEY (user_ar_id) REFERENCES sis_areas (ar_id);

    ALTER TABLE sis_roles_usuarios ADD CONSTRAINT sis_roles_usuarios_fk01 FOREIGN KEY (sru_rol_id) REFERENCES sis_roles (rol_id);

    ALTER TABLE sis_roles_usuarios ADD CONSTRAINT sis_roles_usuarios_fk02 FOREIGN KEY (sru_user_id) REFERENCES sis_usuarios (user_id);

    ALTER TABLE sis_mensajes ADD CONSTRAINT sis_mensajes_fk01 FOREIGN KEY (sms_usu_id_emisor) REFERENCES sis_usuarios (user_id);

    ALTER TABLE sis_mensajes ADD CONSTRAINT sis_mensajes_fk02 FOREIGN KEY (sms_chat_id) REFERENCES sis_chats (cht_id);

    ALTER TABLE sis_chats ADD CONSTRAINT sis_chats_fk01 FOREIGN KEY (cht_emisor_id) REFERENCES sis_usuarios (user_id);

    ALTER TABLE sis_chats ADD CONSTRAINT sis_chats_fk02 FOREIGN KEY (cht_receptor_id) REFERENCES sis_usuarios (user_id);

    ALTER TABLE sis_variables ADD CONSTRAINT sis_variables_fk01 FOREIGN KEY (var_not_id) REFERENCES sis_notificacion (not_id);

    ALTER TABLE sis_variables_condicionales ADD CONSTRAINT sis_variables_condicionales_fk01 FOREIGN KEY (vcon_var_id) REFERENCES sis_variables (var_id);

    ALTER TABLE sis_variables_multimedia ADD CONSTRAINT sis_variables_multimedia_fk01 FOREIGN KEY (media_var_id) REFERENCES sis_variables (var_id);

    ALTER TABLE sis_correos ADD CONSTRAINT sis_correos_fk01 FOREIGN KEY (cor_not_id) REFERENCES sis_notificacion (not_id);

    ALTER TABLE sis_gestion ADD CONSTRAINT sis_gestion_fk01 FOREIGN KEY (ges_solicitante_id) REFERENCES sis_usuarios (user_id);

    ALTER TABLE sis_gestion ADD CONSTRAINT sis_gestion_fk02 FOREIGN KEY (ges_asignado_id) REFERENCES sis_usuarios (user_id);

    ALTER TABLE sis_gestion ADD CONSTRAINT sis_gestion_fk03 FOREIGN KEY (ges_sub_id) REFERENCES sis_subactividades (sub_id);

    ALTER TABLE sis_gestion ADD CONSTRAINT sis_gestion_fk04 FOREIGN KEY (ges_act_id) REFERENCES sis_actividades (act_id);

    ALTER TABLE sis_actividades ADD CONSTRAINT sis_actividades_fk01 FOREIGN KEY (act_ar_id) REFERENCES sis_areas (ar_id);

    ALTER TABLE sis_subactividades ADD CONSTRAINT sis_subactividades_fk01 FOREIGN KEY (sub_act_id) REFERENCES sis_actividades (act_id);

    ALTER TABLE sis_seguimiento ADD CONSTRAINT sis_seguimiento_fk01 FOREIGN KEY (seg_user_id) REFERENCES sis_usuarios (user_id);

    ALTER TABLE sis_seguimiento ADD CONSTRAINT sis_seguimiento_fk02 FOREIGN KEY (seg_ges_id) REFERENCES sis_gestion (ges_id);

    ALTER TABLE sis_aprobaciones ADD CONSTRAINT sis_aprobaciones_fk01 FOREIGN KEY (apro_user_id) REFERENCES sis_usuarios (user_id);

    ALTER TABLE sis_aprobaciones ADD CONSTRAINT sis_aprobaciones_fk02 FOREIGN KEY (apro_ges_id) REFERENCES sis_gestion (ges_id);

    ALTER TABLE sis_aprobadores ADD CONSTRAINT sis_aprobadores_fk01 FOREIGN KEY (sgu_user_id) REFERENCES sis_usuarios (user_id);

    ALTER TABLE sis_aprobadores ADD CONSTRAINT sis_aprobadores_fk02 FOREIGN KEY (sgu_ges_id) REFERENCES sis_gestion (ges_id);

    --Triggers 

    -- Triggers para la tabla sis_sistemas -------------------------------

    CREATE OR REPLACE TRIGGER sis_sistemas_trg01
    BEFORE INSERT ON sis_sistemas FOR EACH ROW
    BEGIN
        IF :new.sis_id IS NULL OR :new.sis_id <= 0 THEN
        :new.sis_id := sis_sistemas_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_sistemas_trg02
    AFTER UPDATE OF sis_id ON sis_sistemas FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20010, 'No se puede actualizar el campo sis_id en la tabla sis_sistemas ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_roles -------------------------------

    CREATE OR REPLACE TRIGGER sis_roles_trg01 
    BEFORE INSERT ON sis_roles FOR EACH ROW
    BEGIN
        IF :new.rol_id IS NULL OR :new.rol_id <= 0 THEN
        :new.rol_id := sis_roles_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_roles_trg02
    AFTER UPDATE OF rol_id ON sis_roles FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20011, 'No se puede actualizar el campo rol_id en la tabla sis_roles ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_usuarios -------------------------------

    CREATE OR REPLACE TRIGGER sis_usuarios_trg01
    BEFORE INSERT ON sis_usuarios FOR EACH ROW
    BEGIN
        IF :new.user_id IS NULL OR :new.user_id <= 0 THEN
        :new.user_id := sis_usuarios_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_usuarios_trg02
    AFTER UPDATE OF user_id ON sis_usuarios FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20012, 'No se puede actualizar el campo user_id en la tabla sis_usuarios ya que utiliza una secuencia.');
    END;


    -- Triggers para la tabla sis_mensajes -------------------------------

    CREATE OR REPLACE TRIGGER sis_mensajes_trg01
    BEFORE INSERT ON sis_mensajes FOR EACH ROW
    BEGIN
        IF :new.sms_id IS NULL OR :new.sms_id <= 0 THEN
        :new.sms_id := sis_mensajes_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_mensajes_trg02
    AFTER UPDATE OF sms_id ON sis_mensajes FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20014, 'No se puede actualizar el campo sms_id en la tabla sis_mensajes ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_chats -------------------------------

    CREATE OR REPLACE TRIGGER sis_chats_trg01
    BEFORE INSERT ON sis_chats FOR EACH ROW
    BEGIN
        IF :new.cht_id IS NULL OR :new.cht_id <= 0 THEN
        :new.cht_id := sis_chats_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_chats_trg02
    AFTER UPDATE OF cht_id ON sis_chats FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20015, 'No se puede actualizar el campo cht_id en la tabla sis_chats ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_parametros -------------------------------

    CREATE OR REPLACE TRIGGER sis_parametros_trg01
    BEFORE INSERT ON sis_parametros FOR EACH ROW
    BEGIN
        IF :new.par_id IS NULL OR :new.par_id <= 0 THEN
        :new.par_id := sis_parametros_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_parametros_trg02
    AFTER UPDATE OF par_id ON sis_parametros FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20016, 'No se puede actualizar el campo par_id en la tabla sis_parametros ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_notificacion -------------------------------

    CREATE OR REPLACE TRIGGER sis_notificacion_trg01
    BEFORE INSERT ON sis_notificacion FOR EACH ROW
    BEGIN
        IF :new.not_id IS NULL OR :new.not_id <= 0 THEN
        :new.not_id := sis_notificacion_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_notificacion_trg02
    AFTER UPDATE OF not_id ON sis_notificacion FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20017, 'No se puede actualizar el campo not_id en la tabla sis_notificacion ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_variables -------------------------------

    CREATE OR REPLACE TRIGGER sis_variables_trg01
    BEFORE INSERT ON sis_variables FOR EACH ROW
    BEGIN
        IF :new.var_id IS NULL OR :new.var_id <= 0 THEN
        :new.var_id := sis_variables_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_variables_trg02
    AFTER UPDATE OF var_id ON sis_variables FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20018, 'No se puede actualizar el campo var_id en la tabla sis_variables ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_variables_condicionales -------------------------------

    CREATE OR REPLACE TRIGGER sis_variables_condicionales_trg01
    BEFORE INSERT ON sis_variables_condicionales FOR EACH ROW
    BEGIN
        IF :new.vcon_id IS NULL OR :new.vcon_id <= 0 THEN
        :new.vcon_id := sis_variables_condicionales_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_variables_condicionales_trg02
    AFTER UPDATE OF vcon_id ON sis_variables_condicionales FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20019, 'No se puede actualizar el campo vcon_id en la tabla sis_variables_condicionales ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_variables_multimedia -------------------------------

    CREATE OR REPLACE TRIGGER sis_variables_multimedia_trg01
    BEFORE INSERT ON sis_variables_multimedia FOR EACH ROW
    BEGIN
        IF :new.media_id IS NULL OR :new.media_id <= 0 THEN
        :new.media_id := sis_variables_multimedia_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_variables_multimedia_trg02
    AFTER UPDATE OF media_id ON sis_variables_multimedia FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20020, 'No se puede actualizar el campo media_id en la tabla sis_variables_multimedia ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_correos -------------------------------

    CREATE OR REPLACE TRIGGER sis_correos_trg01
    BEFORE INSERT ON sis_correos FOR EACH ROW
    BEGIN
        IF :new.cor_id IS NULL OR :new.cor_id <= 0 THEN
        :new.cor_id := sis_correos_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_correos_trg02
    AFTER UPDATE OF cor_id ON sis_correos FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20021, 'No se puede actualizar el campo cor_id en la tabla sis_correos ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_gestion -------------------------------

    CREATE OR REPLACE TRIGGER sis_gestion_trg01
    BEFORE INSERT ON sis_gestion FOR EACH ROW
    BEGIN
        IF :new.ges_id IS NULL OR :new.ges_id <= 0 THEN
        :new.ges_id := sis_gestion_seq01.NEXTVAL;
        END IF;
    END;

    CREATE OR REPLACE TRIGGER sis_gestion_trg02
    AFTER UPDATE OF ges_id ON sis_gestion FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20022, 'No se puede actualizar el campo ges_id en la tabla sis_gestion ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_areas -------------------------------
    CREATE OR REPLACE TRIGGER sis_areas_trg01
    BEFORE INSERT ON sis_areas FOR EACH ROW
    BEGIN
        IF :new.ar_id IS NULL OR :new.ar_id <= 0 THEN
        :new.ar_id := sis_areas_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_areas_trg02
    AFTER UPDATE OF ar_id ON sis_areas FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20023, 'No se puede actualizar el campo ar_id en la tabla sis_areas ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_actividades -------------------------------
    CREATE OR REPLACE TRIGGER sis_actividades_trg01
    BEFORE INSERT ON sis_actividades FOR EACH ROW
    BEGIN
        IF :new.act_id IS NULL OR :new.act_id <= 0 THEN
        :new.act_id := sis_actividades_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_actividades_trg02
    AFTER UPDATE OF act_id ON sis_actividades FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20024, 'No se puede actualizar el campo act_id en la tabla sis_actividades ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_subactividades -------------------------------
    CREATE OR REPLACE TRIGGER sis_subactividades_trg01
    BEFORE INSERT ON sis_subactividades FOR EACH ROW
    BEGIN
        IF :new.sub_id IS NULL OR :new.sub_id <= 0 THEN
        :new.sub_id := sis_subactividades_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_subactividades_trg02
    AFTER UPDATE OF sub_id ON sis_subactividades FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20025, 'No se puede actualizar el campo sub_id en la tabla sis_subactividades ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_seguimiento -------------------------------
    CREATE OR REPLACE TRIGGER sis_seguimiento_trg01
    BEFORE INSERT ON sis_seguimiento FOR EACH ROW
    BEGIN
        IF :new.seg_id IS NULL OR :new.seg_id <= 0 THEN
        :new.seg_id := sis_seguimiento_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_seguimiento_trg02
    AFTER UPDATE OF seg_id ON sis_seguimiento FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20026, 'No se puede actualizar el campo seg_id en la tabla sis_seguimiento ya que utiliza una secuencia.');
    END;

    -- Triggers para la tabla sis_aprobaciones -------------------------------
    CREATE OR REPLACE TRIGGER sis_aprobaciones_trg01
    BEFORE INSERT ON sis_aprobaciones FOR EACH ROW
    BEGIN
        IF :new.apro_id IS NULL OR :new.apro_id <= 0 THEN
        :new.apro_id := sis_aprobaciones_seq01.NEXTVAL;
        END IF;
    END;
    ;

    CREATE OR REPLACE TRIGGER sis_aprobaciones_trg02
    AFTER UPDATE OF apro_id ON sis_aprobaciones FOR EACH ROW
    BEGIN
        RAISE_APPLICATION_ERROR(-20027, 'No se puede actualizar el campo apro_id en la tabla sis_aprobaciones ya que utiliza una secuencia.');
    END;


    -- Inserción para sis_parametros
    INSERT INTO sis_parametros (par_correo, par_clave, par_puerto, par_server, par_protocolo, par_timeout, par_version)
    VALUES ('sigeceuna87@gmail.com', 'ptwq yxai aigf qpwp', 587, 'smtp.gmail.com', 'smtp', 15, 1);

    -- Inserción para sis_notificacion
    INSERT INTO sis_notificacion (not_nombre, not_plantilla, not_version)
    VALUES ('Correos Comunes', 'Correos Comunes', 1);

    INSERT INTO sis_notificacion (not_nombre, not_plantilla, not_version)
    VALUES ('Activacion de Cuenta',
    '<!DOCTYPE html> <html lang="es"> <head> <meta charset="UTF-8"> <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <title>Activacion de Cuenta</title> <style> body { font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; 
    margin: 0; padding: 0; } .container { max-width: 650px; margin: 30px auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; 
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); border: 1px solid #e0e0e0; } .header { background-color: #0D47A1; padding: 30px 20px; text-align: center; 
    color: white; } .header img { width: 200px; height: auto; margin-bottom: 15px; } .header h2 { font-size: 26px; margin: 0; font-weight: normal; } 
    .content { padding: 40px 30px; text-align: left; } .content h1 { font-size: 24px; color: #0D47A1; margin-bottom: 15px; } .content p { font-size: 16px; 
    color: #5f6368; margin-bottom: 20px; line-height: 1.6; } .cta { text-align: center; } .cta a { display: inline-block; text-decoration: none; 
    background-color: #0D47A1; color: white; padding: 12px 30px; font-size: 18px; border-radius: 6px; transition: background-color 0.3s ease; } 
    .cta a:hover { background-color: #1565C0; } .footer { background-color: #f1f1f1; padding: 20px; text-align: center; font-size: 14px; 
    color: #7a7a7a; } .footer p { margin: 0; } .footer a { color: #0D47A1; text-decoration: none; } .footer a:hover { text-decoration: underline; } 
    .spacer { height: 40px; } </style> </head> <body> <div class="container"> <div class="header"> <img src="https://i.ibb.co/qdw7zmV/Logo-White.png" 
    alt="Logo SigeceUna"> <h2>Bienvenido a SigeceUna</h2> </div> <div class="content"> <h1>¡Activa tu cuenta ahora!</h1> 
    <p>Estimado/a [user],</p> <p>Nos alegra que te hayas registrado en SigeceUna. Para activar tu cuenta y empezar a disfrutar de nuestros servicios, 
    haz clic en el boton de abajo:</p> <div class="cta"> <a href="http://localhost:8080/Api/ws/UsersController/activateUser/[user]" 
    target="_blank">Activar Cuenta</a> </div> <p>Si no solicitaste esta activacion, puedes ignorar este correo. Tu cuenta estara segura.</p> </div> 
    <div class="spacer"></div> <div class="footer"> <p>&copy; 2024 SigeceUna. Todos los derechos reservados.</p> 
    <p><a href="#">Politica de Privacidad</a> | <a href="#">Terminos y Condiciones</a></p> </div> </div> </body> </html>', 1);

    INSERT INTO sis_notificacion (not_nombre, not_plantilla, not_version)
    VALUES ('Recuperacion de Clave',
    '<!DOCTYPE html> <html lang="es"> <head> <meta charset="UTF-8"> <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <title>Recuperación de Contraseña</title> <style> body { font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; 
    margin: 0; padding: 0; } .container { max-width: 650px; margin: 30px auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; 
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); border: 1px solid #e0e0e0; } .header { background-color: #0D47A1; padding: 30px 20px; text-align: center; 
    color: white; } .header img { width: 200px; height: auto; margin-bottom: 15px; } .header h2 { font-size: 26px; margin: 0; font-weight: normal; } 
    .content { padding: 40px 30px; text-align: left; } .content h1 { font-size: 24px; color: #0D47A1; margin-bottom: 15px; } .content p { font-size: 16px; 
    color: #5f6368; margin-bottom: 20px; line-height: 1.6; } .password-box { background-color: #e0f2f1; color: #004d40; font-size: 18px; font-weight: bold; 
    padding: 10px 20px; text-align: center; border-radius: 6px; margin: 20px 0; } .note { font-size: 14px; color: #5f6368; margin-top: 10px; 
    line-height: 1.4; text-align: center; } .footer { background-color: #f1f1f1; padding: 20px; text-align: center; font-size: 14px; color: #7a7a7a; } 
    .footer p { margin: 0; } .footer a { color: #0D47A1; text-decoration: none; } .footer a:hover { text-decoration: underline; } 
    .spacer { height: 40px; } </style> </head> <body> <div class="container"> <div class="header"> 
    <img src="https://i.ibb.co/qdw7zmV/Logo-White.png" alt="Logo SigeceUna"> <h2>Recuperación de Contraseña - SigeceUna</h2> </div> 
    <div class="content"> <h1>Se ha generado una nueva contraseña</h1> <p>Estimado/a [user],</p> <p>Has solicitado recuperar tu contraseña. 
    A continuación, encontrarás una contraseña temporal para acceder a tu cuenta en SigeceUna.</p> 
    <div class="password-box">[contraseña_temporal]</div> <p>Para cambiar tu contraseña, ingresa a la configuración de tu cuenta después de iniciar sesión.</p> 
    <p>Si no solicitaste este cambio de contraseña, por favor ignora este correo o contáctanos de inmediato.</p> <div class="note"> 
    <p><strong>Nota:</strong> Esta contraseña es temporal y debe ser cambiada para proteger tu cuenta.</p> </div> </div> 
    <div class="spacer"></div> <div class="footer"> <p>&copy; 2024 SigeceUna. Todos los derechos reservados.</p> 
    <p><a href="#">Política de Privacidad</a> | <a href="#">Términos y Condiciones</a></p> </div> </div> </body> </html>', 1);

    -- Inserciones para sis_sistemas
    INSERT INTO sis_sistemas (sis_nombre, sis_version)
    VALUES ('Seguridad', 1);

    INSERT INTO sis_sistemas (sis_nombre, sis_version)
    VALUES ('Chats', 1);

    INSERT INTO sis_sistemas (sis_nombre, sis_version)
    VALUES ('Correos Masivos', 1);

    INSERT INTO sis_sistemas (sis_nombre, sis_version)
    VALUES ('Gestiones', 1);
    -- Inserciones para sis_roles
    INSERT INTO sis_roles (rol_nombre, rol_sis_id, rol_version)
    VALUES ('Admin', 1, 1);

    INSERT INTO sis_roles (rol_nombre, rol_sis_id, rol_version)
    VALUES ('Normal', 2, 1);

    INSERT INTO sis_roles (rol_nombre, rol_sis_id, rol_version)
    VALUES ('Administrador de correos masivos', 3, 1);

    INSERT INTO sis_roles (rol_nombre, rol_sis_id, rol_version)
    VALUES ('Normal de correos masivos', 3, 1);

    INSERT INTO sis_roles (rol_nombre, rol_sis_id, rol_version)
    VALUES ('Administrador', 4, 1);

    INSERT INTO sis_roles (rol_nombre, rol_sis_id, rol_version)
    VALUES ('Gestor', 4, 1);

    INSERT INTO sis_roles (rol_nombre, rol_sis_id, rol_version)
    VALUES ('Solicitante', 4, 1);