CREATE TABLE expense (
    id bigserial NOT NULL,
    "date" timestamp NOT NULL,
    description varchar(255) NOT NULL,
    value numeric(19, 2) NOT NULL,
    CONSTRAINT expense_pkey PRIMARY KEY (id)
);

CREATE TABLE income (
   id bigserial NOT NULL,
   "date" timestamp NOT NULL,
   description varchar(255) NOT NULL,
   value numeric(19, 2) NOT NULL,
   CONSTRAINT income_pkey PRIMARY KEY (id)
);