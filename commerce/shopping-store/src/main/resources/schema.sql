drop table if exists products;

create table if not exists products
(
    product_id       uuid default gen_random_uuid() primary key,
    product_name     varchar not null,
    description      varchar not null,
    image_src        varchar,
    quantity_state   varchar not null,
    product_state    varchar not null,
    rating           integer,
    product_category varchar not null,
    price            double precision
);