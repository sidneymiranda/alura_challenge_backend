CREATE TABLE public.expense (
    id bigserial NOT NULL,
    "date" timestamp NOT NULL,
    description varchar(255) NOT NULL,
    value numeric(19, 2) NOT NULL,
    CONSTRAINT expense_pkey PRIMARY KEY (id)
);

CREATE TABLE public.income (
   id bigserial NOT NULL,
   "date" timestamp NOT NULL,
   description varchar(255) NOT NULL,
   value numeric(19, 2) NOT NULL,
   CONSTRAINT income_pkey PRIMARY KEY (id)
);