-- DROP SCHEMA public;

-- CREATE SCHEMA public AUTHORIZATION account_manager_user;

-- DROP SEQUENCE public.expense_id_seq;

CREATE SEQUENCE public.expense_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.hibernate_sequence;

CREATE SEQUENCE public.hibernate_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.income_id_seq;

CREATE SEQUENCE public.income_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
-- DROP SEQUENCE public.users_id_seq;

CREATE SEQUENCE public.users_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;-- public.expense definition

-- Drop table

-- DROP TABLE public.expense;

CREATE TABLE public.expense (
                                id bigserial NOT NULL,
                                category varchar(20) NULL,
                                "date" date NOT NULL,
                                description varchar(255) NOT NULL,
                                value numeric(19, 2) NOT NULL,
                                CONSTRAINT expense_pkey PRIMARY KEY (id)
);


-- public.flyway_schema_history definition

-- Drop table

DROP TABLE public.flyway_schema_history;

CREATE TABLE public.flyway_schema_history (
                                              installed_rank int4 NOT NULL,
                                              "version" varchar(50) NULL,
                                              description varchar(200) NOT NULL,
                                              "type" varchar(20) NOT NULL,
                                              script varchar(1000) NOT NULL,
                                              checksum int4 NULL,
                                              installed_by varchar(100) NOT NULL,
                                              installed_on timestamp NOT NULL DEFAULT now(),
                                              execution_time int4 NOT NULL,
                                              success bool NOT NULL,
                                              CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank)
);
CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


-- public.income definition

-- Drop table

-- DROP TABLE public.income;

CREATE TABLE public.income (
                               id bigserial NOT NULL,
                               "date" date NOT NULL,
                               description varchar(255) NOT NULL,
                               value numeric(19, 2) NOT NULL,
                               CONSTRAINT income_pkey PRIMARY KEY (id)
);


-- public."role" definition

-- Drop table

-- DROP TABLE public."role";

CREATE TABLE public."role" (
                               id int8 NOT NULL,
                               "name" varchar(255) NOT NULL,
                               CONSTRAINT role_pkey PRIMARY KEY (id)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
                              id bigserial NOT NULL,
                              email varchar(255) NULL,
                              "password" varchar(255) NULL,
                              username varchar(255) NULL,
                              CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- public.user_roles definition

-- Drop table

-- DROP TABLE public.user_roles;

CREATE TABLE public.user_roles (
                                   user_id int8 NOT NULL,
                                   role_id int8 NOT NULL,
                                   CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
                                   CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES public."role"(id),
                                   CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);