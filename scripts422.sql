CREATE TABLE car (id bigint not null primary key
    ,brand varchar(100) not null
    ,model varchar(100) not null
    ,price integer not null
    ,constraint brand_model_unique unique (brand, model)
);

CREATE TABLE man (id bigint not null primary key
    ,name varchar(1000) not null
    ,age integer not null constraint age_constraint check (age > 0)
    ,is_rights boolean not null
    ,car_id integer REFERENCES car(id) constraint check_car check (is_rights = true));